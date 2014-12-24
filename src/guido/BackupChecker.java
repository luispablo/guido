package guido;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BackupChecker {
	private static GuidoLogger<BackupChecker> l;
	
	private String backupDir;
	private List<String> backupItems;
	private int port;
	
	public BackupChecker(int port, String backupDir, List<String> backupItems, String logFile) throws SecurityException, IOException {
		this.backupDir = backupDir;
		this.backupItems = backupItems;
		this.port = port;
		
		l = new GuidoLogger<>(BackupChecker.class, logFile);
	}
	
	public BackupChecker startServer() throws IOException {
		try(ServerSocket server = new ServerSocket(port)) {
			while (true) {
				Socket socket = server.accept();
				l.i("Client connected.");
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				
				oos.writeObject(check());
				oos.close();
				socket.close();
			}
		}			
	}
	
	public boolean check() throws IOException {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		l.i("yesterday is: "+ yesterday.toString());
		
		Predicate<Path> validos = new Predicate<Path>() {
			@Override
			public boolean test(Path p) {
		    	boolean visible = !p.getFileName().toString().startsWith(".");

		    	BasicFileAttributes attr;
				try {
					attr = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
					Instant i = attr.lastModifiedTime().toInstant();
					LocalDate res = LocalDateTime.ofInstant(i, ZoneId.systemDefault()).toLocalDate();
					l.i("file "+ p.getFileName().toString() +" date is "+ res.toString());

					boolean ofYesterday = res.equals(yesterday);
			    	
					return visible && ofYesterday;
				} catch (IOException e) {
					l.e(e);
					return false;
				}
			}
		};

		List<Path> foundFiles = Files.list(new File(backupDir).toPath())
										.filter(validos)
										.collect(Collectors.toList());

		l.i("found files: "+ foundFiles.stream().map(p -> p.getFileName().toString()).collect(Collectors.joining(", ")));
		l.i("backup items: "+ backupItems.stream().collect(Collectors.joining(", ")));
		
		boolean result = backupItems.stream()
									.allMatch(item -> foundFiles.stream()
																.anyMatch(p -> p.getFileName().toString().contains(item)));

		l.i("Responding: "+ result);
		return result;
	}

}
