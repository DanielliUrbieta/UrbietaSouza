package hidra.view;

import hidra.control.DepotControl;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;

/**
 * @author Pedro Souza e Danielli Urbieta
 * 
 */
public class MainTestAll {

	// TODO: Fazer javadoc da classe POJO
	/**
	 * @param args
	 * @throws GitAPIException
	 * @throws NoHeadException
	 * @throws RevisionSyntaxException
	 *
	 */
	@SuppressWarnings("nls")
	public static void main(String[] args) throws RevisionSyntaxException,
			NoHeadException, GitAPIException {


		/**
		 * Objeto DepotControl eh a controladora, ela implementa uma interface
		 * com as funcionalidades do projeto Hidra. E inicializa um repositorio
		 * em um diretorio escolhido pelo usuario.
		 */
		DepotControl control = new DepotControl();

		/**
		 * Funcionalidade Add. e utilizada para adicionar um arquivo a area de
		 * selecao do repositorio
		 */

		String filename = control.getSelectDirectory().chooseFile(
				control.getDiretorio());

		try {
			control.add(filename);
		} catch (NoFilepatternException e) {
			System.err.println("Arquivo Invalido: " + e);

		} catch (GitAPIException e) {
			System.err.println("Erro: " + e);
		}

		/**
		 * Funcionalidade Remove e utilizada para remocao de um arquivo da area
		 * de selecao e tambem do diretorio.
		 */

		filename = control.getSelectDirectory().chooseFile(
				control.getDiretorio());

		try {
			control.removeFile(filename);
		} catch (NoWorkTreeException e) {
			System.err.println("Erro: " + e);
		} catch (GitAPIException e) {
			System.err.println("Erro: " + e);
		}

		/**
		 * Funcionalidade Commit e utilizada para submeter as ultimas alteracoes
		 * do codigo fonte ao repositorio e fazer com que estas alteracoes se
		 * tornem parte da versao principal (head) do repositorio.
		 */

		String message = "Mensagem de Commit";
		try {
			control.commit(message);
		} catch (GitAPIException e) {
			System.err.println("Erro: " + e);
		}

		/**
		 * Funcionalidade Status e utilizada para mostrar o estado no qual se
		 * encontra o repositorio
		 */

		try {
			control.status();
		} catch (NoWorkTreeException e) {
			System.err.println("Erro: " + e);
		} catch (GitAPIException e) {
			System.err.println("Erro: " + e);
		}

		/**
		 * Funcionalidade GetLogs e utilizada para mostrar ao usuario um
		 * conjunto de Logs do repositorio utilizado
		 */

		control.getLogs();

		/**
		 * Funcionalidade Create Branch e utilizada para criacao de um novo
		 * Branch ao repositorio utilizado
		 */

		String branchName = "NovoBranch";
		control.createBranch(branchName);

		/**
		 * Funcionalidade Show Branch mostra ao usuario uma lista com todos os
		 * branchs pertencentes ao repositorio utilizado.
		 */
		control.showBranch();

		/**
		 * Funcionalidade Delete Branch e utilizada para remocao de um branch do
		 * repositorio utilizado.
		 */

		String branchRemove = "NovoBranch";
		control.deleteBranch(branchRemove);
		control.showBranch();

		/**
		 * Funcionalidade ShowDiffBranchs e utilizada para mostrar a diferenca
		 * entre dois branchs de um mesmo repositorio.
		 */

		control.createBranch("branch1");
		control.createBranch("branch2");
		control.getDiff("branch1", "branch2");

		/**
		 * Funcionalidade Pull e Push utilizadas respectivamente para atualizar
		 * repositorio local com repositorio remoto e envio de alteracoes que
		 * passaram por commit entre repositorio local e repositorio remoto.
		 */

		// PS: Necessitam de configuracao manual via terminal.
		// Sera criado um script para automatizar o processo.

		/**
		 * Git git = control.getHidra().getGit(); control.pull(git);
		 * control.push(git);
		 */
	}

}
