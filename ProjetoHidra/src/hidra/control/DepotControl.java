package hidra.control;

import hidra.model.Depot;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.CannotDeleteCurrentBranchException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NotMergedException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import selectFilesAndDirectories.Select;

/**
 * @author pedro
 *
 */
public class DepotControl implements IDepot<Depot> {

	/*
	 * atributos da classe
	 */
	/**
	 *
	 */
	private Depot hidra;

	/**
	 *
	 */
	private Select selectDirectory;

	/* Getters and Setters */

	/**
	 * @return hidra
	 */
	public Depot getHidra() {
		return hidra;
	}

	/**
	 * @param hidra
	 */
	public void setHidra(Depot hidra) {
		this.hidra = hidra;
	}

	/**
	 * @return selectDirectory
	 */
	public Select getSelectDirectory() {
		return selectDirectory;
	}

	/**
	 * @param selectDirectory
	 */
	public void setSelectDirectory(Select selectDirectory) {
		this.selectDirectory = selectDirectory;
	}

	/**
	 * @return diretorio
	 *
	 */
	public File getDiretorio() {
		return hidra.getGit().getRepository().getDirectory().getParentFile();
	}

	/**
	 * construtor de classe
	 */

	public DepotControl() {

		hidra = new Depot();
		selectDirectory = new Select();
		try {
			hidra.setLocalPath(selectDirectory.chooseDirectory());
		} catch (IOException e) {

			e.printStackTrace();
		}
		hidra.init1();

	}

	public void init(Depot myDepot) {

		File dir = new File(myDepot.getLocalPath());
		InitCommand initCommand = Git.init();
		initCommand.setDirectory(dir);

		try {
			myDepot.setGit(initCommand.call());
		} catch (GitAPIException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("nls")
	public void create(Depot myDepot) throws IOException {
		myDepot.setrepository(new FileRepository(myDepot.getLocalPath()
				+ "/.git"));
		myDepot.getrepository().create();
	}

	public void add(String file) throws NoFilepatternException,
			GitAPIException {

		this.hidra.getGit().add().addFilepattern(file).call();
		this.hidra.setAdded(hidra.getGit().status().call().getAdded());
		JOptionPane.showMessageDialog(null, this.hidra.getAdded());
	}


	public void commit(String message) throws GitAPIException {

		RevCommit commit = this.hidra.getGit().commit().setMessage(message)
				.call();
		JOptionPane.showMessageDialog(null, commit.getId().getName());

	}

	// NOTE: string file é o arquivo.extensão

	public void removeFile(String file)
			throws NoWorkTreeException, GitAPIException {

		this.hidra.getGit().rm().addFilepattern(file).call();
		this.hidra.setRemoved(this.hidra.getGit().status().call()
				.getUntracked());
		if (this.hidra.getRemoved() != null)
			JOptionPane.showMessageDialog(null,
					"File " + file + "successfully removed "); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void clone(Depot myDepot) throws InvalidRemoteException,
			TransportException, GitAPIException, IOException {

		File file = new File(myDepot.getDiretoryToCopy());
		Git.cloneRepository().setURI(myDepot.getRemotePath())
				.setDirectory(file).call();

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository1 = builder.setGitDir(file).readEnvironment()
				.findGitDir().build();

		JOptionPane.showMessageDialog(null,
				"Having repository: " + repository1.getDirectory()); //$NON-NLS-1$

		repository1.close();
	}

	@SuppressWarnings("nls")
	public void status() throws NoWorkTreeException,
			GitAPIException {

		hidra.setStatus(this.hidra.getGit().status().call());

		JOptionPane.showMessageDialog(null, "Added: "
				+ this.hidra.getStatus().getAdded() + "\nChanged"
				+ this.hidra.getStatus().getChanged() + "\nConflicting: "
				+ this.hidra.getStatus().getConflicting()
				+ "\nConflictingStageState: "
				+ this.hidra.getStatus().getConflictingStageState()
				+ "\nIgnoredNotInIndex: "
				+ this.hidra.getStatus().getIgnoredNotInIndex() + "\nMissing: "
				+ this.hidra.getStatus().getMissing() + "\nModified: "
				+ this.hidra.getStatus().getModified() + "\nRemoved: "
				+ this.hidra.getStatus().getRemoved() + "\nUntracked: "
				+ this.hidra.getStatus().getUntracked()
				+ "\nUntrackedFolders: "
				+ this.hidra.getStatus().getUntrackedFolders()
				+ "\nUncommitted Changes"
				+ this.hidra.getStatus().getUncommittedChanges());

		// JOptionPane.showInternalMessageDialog(null, "s", title, messageType);
	}

	public void getLogs() throws NoHeadException, GitAPIException,
			RevisionSyntaxException {

		Git git1 = this.hidra.init1();
		// Repository repository1 = git1.getRepository();
		//ObjectId head = repository1.resolve("HEAD"); //$NON-NLS-1$
		Iterable<RevCommit> log1 = git1.log().call();

		Iterator itr = log1.iterator();

		while (itr.hasNext()) {
			// Object element = itr.next();
			RevCommit rev = (RevCommit) itr.next();
			// System.out.println(element);
			System.out.println("Author: " + rev.getAuthorIdent().getName()); //$NON-NLS-1$
			System.out.println("Message: " + rev.getFullMessage()); //$NON-NLS-1$
			System.out.println();
		}

	}

	@SuppressWarnings({ "null" })
	public void showBranch() {

		Git git1 = this.hidra.init1();
		Repository repository1 = git1.getRepository();
		List<org.eclipse.jgit.lib.Ref> call = null;
		try {
			call = new Git(repository1).branchList().call();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// repensar mostrar ou não o id do branch
		for (org.eclipse.jgit.lib.Ref ref : call) {
			System.out.println("Branch: " + ref + " " + ref.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		repository1.close();

	}

	@SuppressWarnings({ "null" })
	public void createBranch(String nameBranch) {

		Git git1 = this.hidra.init1();
		Repository repository1 = git1.getRepository();
		try {
			git1.branchCreate().setName(nameBranch).call();

		} catch (RefAlreadyExistsException e1) {

			e1.printStackTrace();
		} catch (RefNotFoundException e1) {

			e1.printStackTrace();
		} catch (InvalidRefNameException e1) {

			e1.printStackTrace();
		} catch (GitAPIException e1) {

			e1.printStackTrace();
		}

		List<org.eclipse.jgit.lib.Ref> call = null;
		try {
			call = new Git(repository1).branchList().call();
		} catch (GitAPIException e) {

			e.printStackTrace();
		}

		for (org.eclipse.jgit.lib.Ref ref : call) {
			System.out.println("Branch Created: " + " " + ref.getName()); //$NON-NLS-1$ //$NON-NLS-2$

		}
		repository1.close();

	}

	public void deleteBranch(String nameBranch) {

		Git git1 = this.hidra.init1();

		try {
			git1.branchDelete().setBranchNames(nameBranch).call();
		} catch (NotMergedException e) {

			e.printStackTrace();
		} catch (CannotDeleteCurrentBranchException e) {

			e.printStackTrace();
		} catch (GitAPIException e) {

			e.printStackTrace();
		}

	}

	// Verificar o que
	/**
	 * @param myDepot
	 * @param localPath
	 * @param remotePath
	 */

	public void pull(Depot myDepot, String localPath, String remotePath) {
		// Repository repository = myDepot.getRepository();
		Git git = myDepot.getGit();
		try {
			PullResult pullResult = git.pull().setRemote(remotePath).call();
			System.out.println(pullResult);
			MergeResult mergeResult = pullResult.getMergeResult();
			if (mergeResult != null) {
				// do something
			}
			RebaseResult rebaseResult = pullResult.getRebaseResult();
			if (rebaseResult != null) {
				// do something
			}

		} catch (WrongRepositoryStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DetachedHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RefNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("null")
	public void getDiff(String branch1, String branch2) {
		//myDepot.setLocalPath("/home/danielli/testando"); //$NON-NLS-1$
		// myDepot.init1();
		Git gitDiff = this.hidra.getGit();
		Repository repo = gitDiff.getRepository();
		AbstractTreeIterator oldTreeParser = prepareTreeParser(repo,
				"refs/heads/" + branch1); //$NON-NLS-1$
		AbstractTreeIterator newTreeParser = prepareTreeParser(repo,
				"refs/heads/" + branch2); //$NON-NLS-1$

		// then the procelain diff-command returns a list of diff entries
		List<DiffEntry> diff = null;
		try {
			diff = new Git(repo).diff().setOldTree(oldTreeParser)
					.setNewTree(newTreeParser).call();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (DiffEntry entry : diff) {
			System.out.println("Entry: " + entry); //$NON-NLS-1$
		}

		repo.close();
	}

	@SuppressWarnings("null")
	public AbstractTreeIterator prepareTreeParser(Repository repository,
			String ref) {
		Ref head = null;
		try {
			head = repository.getRef(ref);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RevWalk walk = new RevWalk(repository);
		RevCommit commit = null;
		try {
			commit = walk.parseCommit(head.getObjectId());
		} catch (MissingObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RevTree tree = null;
		try {
			tree = walk.parseTree(commit.getTree().getId());
		} catch (MissingObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
		ObjectReader oldReader = repository.newObjectReader();
		try {
			try {
				oldTreeParser.reset(oldReader, tree.getId());
			} catch (IncorrectObjectTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			oldReader.release();
		}
		return oldTreeParser;
	}

	/**
	 * @param gitPush
	 */
	@SuppressWarnings("null")
	public void push(Git gitPush) {

		// Colocar usuario e senha nos "", ""
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(
"", ""); //$NON-NLS-1$//$NON-NLS-2$
		PushCommand pc = gitPush.push();
		pc.setCredentialsProvider(cp).setForce(true).setPushAll();
		Iterator<PushResult> it = null;
		try {
			it = pc.call().iterator();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}

	/**
	 * @param git
	 */
	public void pull(Git git) {

		PullResult pullResult = null;
		try {
			pullResult = git.pull().call();
		} catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				System.out.println(pullResult);
		@SuppressWarnings("null")
				MergeResult mergeResult = pullResult.getMergeResult();
				if (mergeResult != null) {
					// do something
				}
				RebaseResult rebaseResult = pullResult.getRebaseResult();
				if (rebaseResult != null) {
					// do something
				}


	}

}
