package tharOS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Main {
	// initialisation de la fenêtre et du HDD à surveiller
	public static String adresse = new String("F:");
	// on génère le fichier
	public static File di = new File(adresse+"/diskInfo.html");


	public static void main(String[] args) {
		try {
			di.createNewFile();
			di.setWritable(true);
			// on rend visible notre interface
			// on définit l'exécuteur
			final ScheduledExecutorService ES = Executors.newSingleThreadScheduledExecutor();
			// on paramètre l'exécuteur ; une action toutes les 10 secondes, n'accepte pas de params
			ES.scheduleAtFixedRate(Main::loop, 0, 10, TimeUnit.SECONDS);
		} catch (IOException e) {
			// on fait rien si ça échoue, le programme se stoppe	
		}
	}
	
	// procédure répétée à chaque tick d'ES
	public static void loop() {
		try {
			enregistrer();
		} catch (IOException e) {
			// Oh, oh.
			promptInformation("Une erreur est survenue.");
		}
	}
	
	public static void enregistrer() throws IOException {
		// blabla paramètres ENNUYEUX blabla
		File disque = new File(adresse);
		long debitOBS = 35000;
		long debit = (debitOBS*1024)/8; // ops
		long espaceLibre = disque.getFreeSpace();
		long espaceTotal = disque.getTotalSpace();
		long temps = (espaceLibre/debit)/(60*60);
		long espaceLibreGo = espaceLibre/(1024*1024*1024);
		long espaceTotalGo = espaceTotal/(1024*1024*1024);
		String couleur = (temps<=4.00)?"red":"green";
		// on écrit dans le fichier
		PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(di)));
		printer.flush();
		// html
		printer.println("<head>");
		printer.println("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"10;URL=F:/diskInfo.html\">");
		printer.println("</head>");
		printer.println("<body style=\"background-color:"+couleur+";\">");
		printer.println("<h1 align=\"center\">");
		printer.println(espaceLibreGo+" Go / "+espaceTotalGo+" Go");
		printer.println("</h1>");
		printer.println("<p align=\"center\">");
		printer.println("Avec un débit de "+debitOBS+" Kbps, il reste "+temps+" heures d'enregistrement.");
		printer.println("</p>");
		printer.println("</body>");
		printer.close();
	}
	
	
	// une méthode pour afficher une boite de dialogue TRES informative
	public static void promptInformation(String texte) {
		JOptionPane info = new JOptionPane(texte,JOptionPane.INFORMATION_MESSAGE);
		JDialog dialoge = info.createDialog("Information");
		dialoge.setAlwaysOnTop(true);
		dialoge.setVisible(true);
	}
	
}
