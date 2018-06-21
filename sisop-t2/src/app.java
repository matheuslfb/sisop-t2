import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.util.Scanner;

public class app {

	public static String mode;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Programa iniciado:  ");

		String arquivo = readFile();
		
		parseFile(arquivo);
		
		//finish
        System.out.println("Finalizado");
		

	}

	private static void parseFile(String arquivo) {

		File f = new File(arquivo);
		FileReader fr;

		Runnable programMode;
		System.out.println("parse file ok");

		try {
			fr = new FileReader(f);
			
			BufferedReader br = new BufferedReader(fr);
			
			if(br.ready()){
				
				memoryManager(br);				
			}
			
			br.close();
            fr.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void memoryManager(BufferedReader br) throws IOException{
		mode = br.readLine().toLowerCase();
		
		String algo = br.readLine().toLowerCase();
        int pageSize = Integer.parseInt(br.readLine());
        int memorySize = Integer.parseInt(br.readLine());
        int diskSize = Integer.parseInt(br.readLine());
        System.out.println("Modo de execução: "+mode+ "alguma coisa: "+algo + " Tamanho da pagina:"+ pageSize + " Tamanho da memória: "+memorySize + "Tamanho do disco: " + diskSize);

	}
	
	private static void modoExecucao(){
		
	}

	private static String readFile() throws FileNotFoundException {

		Scanner entrada = new Scanner(new FileReader("src\\entrada1.txt"));
		System.out.println("ok");

		return entrada.nextLine();
	}

}
