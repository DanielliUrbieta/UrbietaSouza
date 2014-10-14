package hidra.control;

import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;

/**
 * @author Danielli Urbieta e Pedro Souza
 * @param <Depot>
 * 
 */
public interface IDepot<Depot> {

	/**
	 * @param myRepo
	 *
	 *
	 */
	public void init(Depot myRepo);


	/**
	 * @param myDepot
	 * @throws IOException
	 */
	public void create(Depot myDepot) throws IOException;

	/**
	 * @param file
	 * @throws NoFilepatternException
	 * @throws GitAPIException
	 * @throws IOException
	 */
	public void add(String file) throws NoFilepatternException,
			GitAPIException, IOException;

	/**
	 * @param message
	 * @throws GitAPIException
	 */
	public void commit(String message) throws GitAPIException;

	/**
	 * @param file
	 * @throws NoWorkTreeException
	 * @throws GitAPIException
	 */
	public void removeFile(String file)
			throws NoWorkTreeException, GitAPIException;

	/**
	 * @param myDepot
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws GitAPIException
	 * @throws IOException
	 */
	public void clone(Depot myDepot) throws InvalidRemoteException,
			TransportException, GitAPIException, IOException;

	/**
	 *
	 * @throws NoWorkTreeException
	 * @throws GitAPIException
	 *
	 */
	public void status() throws NoWorkTreeException,
			GitAPIException;

	/**
	 * @throws NoHeadException
	 * @throws GitAPIException
	 * @throws RevisionSyntaxException
	 * @throws AmbiguousObjectException
	 * @throws IOException
	 *
	 */
	public void getLogs() throws NoHeadException, GitAPIException,
			RevisionSyntaxException, AmbiguousObjectException, IOException;

	/**
	 */
	public void showBranch();

	/**
	 * @param nameBranch
	 */
	public void createBranch(String nameBranch);

	/**
	 * @param nameBranch
	 */
	public void deleteBranch(String nameBranch);


	/**
	 * @param branch1
	 * @param branch2
	 */
	public void getDiff(String branch1, String branch2);

	/**
	 * @param repository
	 * @param ref
	 * @return abstractTreeIterators
	 */
	public AbstractTreeIterator prepareTreeParser(Repository repository,
			String ref);

	/**
	 * @param git
	 */
	public void pull(Git git);

}
