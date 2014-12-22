package guido;

import static java.util.stream.Collectors.toList;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Main {
	private static GuidoLogger<Main> l;
	
	private static final String BACKUP_DIR = "backupDir";
	private static final String BACKUP_ITEMS = "backupItems";
	private static final String PORT = "port";
	
	public static void main(String[] args) {
		try {
			l = new GuidoLogger<>(Main.class, args[1]);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		l.i("Guido running: "+ ManagementFactory.getRuntimeMXBean().getName());
		startServer(args[0], args[1]);
	}
		
	private static void startServer(String propertiesFileName, String logFileName) {
		l.i("Taking properties from file: "+ propertiesFileName);
		Properties prop = new Properties();

		try (FileInputStream fis = new FileInputStream(propertiesFileName)) { 
			prop.load(fis);
			
			String backupDir = prop.getProperty(BACKUP_DIR);
			List<String> backupItems = Arrays.asList(prop.getProperty(BACKUP_ITEMS).split(",")).stream()
																								.map(String::trim)
																								.collect(toList());
			int port = Integer.parseInt(prop.getProperty(PORT).trim());
			
			new BackupChecker(port, backupDir, backupItems, logFileName).startServer();
			
		} catch (IOException e) {
			l.e(e);
			System.exit(1);
		}
	}
	

}
