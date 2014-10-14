package hidra.view;

import hidra.control.DepotControl;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;

/**
 * @author Danielli Urbieta e Pedro Souza
 * 
 */
public class Main {

	/**
	 * @param args
	 * @throws IOException
	 * @throws GitAPIException
	 * @throws NoFilepatternException
	 */
	@SuppressWarnings("nls")
	public static void main(String[] args) throws IOException,
			NoFilepatternException, GitAPIException {

		String filename = null;
		String message = null;
		String branchName = null;

		String branch1 = null;
		String branch2 = null;



		/* Menu de Boas Vindas Projeto HIDRA. */
		JOptionPane.showMessageDialog(null, "Bem vindo ao HIDRA ."
				+ "\nEscolha o repositorio");

		/* obj de controle, contem os métodos do JGit. */
		DepotControl controle = new DepotControl();

		/* Menu com os métodos do DepotControl */
		int opcao;
		do {
			opcao = Integer.parseInt(JOptionPane.showInputDialog(""
					+ "1 - inicializar como repositorio.\n"
					+ "2 - Create new Repository.\n" + "3 - Add file.\n"
					+ "4 - Remove file.\n" + "5 - Commit.\n"
					+ "6 - Clone Repository.\n" + "7 - Show Status.\n"
					+ "8 - Log.\n" + "9 - Show Branch.\n"
					+ "10 - Delete Branch.\n" + "11 - Create Branch.\n"
					+ "12 - Diff Branch.\n" + "13 - Sair"));

			switch (opcao) {
			case 1:

				break;

			case 2:

				break;

			case 3:
				JOptionPane.showMessageDialog(null,
						"Escolha o arquivo a adicionar");

				filename = controle.getSelectDirectory().chooseFile(
						controle.getDiretorio());
				controle.add(filename);

				break;

			case 4:
				JOptionPane.showMessageDialog(null,
						"Escolha o arquivo a remover");

				filename = controle.getSelectDirectory().chooseFile(
						controle.getDiretorio());
				controle.removeFile(filename);

				break;

			case 5:

				message = JOptionPane
						.showInputDialog("Digite a mensagem do commit");
				controle.commit(message);
				break;

			case 6: /*
					 * obs: testar método clone. o objto Depot possui atributo
					 * DiretoryToCopy que é a string do localpath para ser
					 * clonado. ou seja, aonde a pasta vai ficar.
					 */

				/*
				 * JOptionPane.showMessageDialog(null,
				 * "Selecione a pasta aonde o repositório será clonado");
				 * newRepo.setDiretoryToCopy(escolher.chooseDirectory());
				 *
				 * newRepo.setRemotePath(JOptionPane .showInputDialog(
				 * "Entre com o endereço do repositorio a ser clonado" ));
				 *
				 * ctrlRepo.clone(newRepo);
				 */
				break;

			case 7:

				controle.status();

				break;

			case 8:

				controle.getLogs();
				break;

			case 9:

				controle.showBranch();
				break;

			case 10:

				branchName = JOptionPane
						.showInputDialog("Nome do Branch a ser removido:");

				controle.deleteBranch(branchName);

				break;

			case 11:
				branchName = JOptionPane
						.showInputDialog("Nome do novo Branch:");
				controle.createBranch(branchName);
				break;

			case 12:

				branch1 = JOptionPane
						.showInputDialog("Nome do primeiro Branch:");
				branch2 = JOptionPane
						.showInputDialog("Nome do segundo Branch:");
				controle.getDiff(branch1, branch2);
				break;

			// case 14: System.out.println("Agora vai");

			case 20:
				/*
				 * String user = JOptionPane
				 * .showInputDialog("Digite seu usuario:"); String pwd =
				 * JOptionPane .showInputDialog("Digite sua sena:"); String url
				 * = JOptionPane .showInputDialog("Digite a url");
				 * controle.push(gitPush);
				 */

				Git git = controle.getHidra().getGit();
				controle.pull(git);
				controle.push(git);
			}
		} while (opcao != 14);

	}
}
