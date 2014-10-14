package selectFilesAndDirectories;


import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;



import java.io.IOException;

import javax.swing.JFileChooser;

//import javax.swing.JOptionPane;

/**
 * @author danielli
 *
 */
public class Select {
	/**
	 * @param directory
	 * @return arquivo selecionado
	 *
	 */
	public String chooseFile(File directory) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(directory);
		int retorno = fileChooser.showOpenDialog(null);

		if (retorno == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			System.out.println(file.getParent());

			return file.getName();
		} else {
			return null;
		}
	}

	/**
	 * Seleciona um diretorio e devolve seu caminho
	 *
	 * @return caminho do diretorio
	 * @throws IOException
	 */
	public String chooseDirectory() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int retorno = fileChooser.showOpenDialog(null);

		if (retorno == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			System.out.println(file.getName());

			return file.getAbsolutePath();
		} else {
			return null;
		}
	}
}
