package id.web.himawari.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import id.web.himawari.processor.impl.FlacProcessor;
import id.web.himawari.processor.impl.Mp3Processor;
import id.web.himawari.processor.impl.Mp4Processor;
import id.web.himawari.util.MainPaneLog;
import id.web.himawari.util.MimeType;
import id.web.himawari.util.ProcessAudioFileUtil;

/**
 * 
 * @author joseph.tarigan@gmail.com
 *
 */
public class MainPane implements MainPaneLog {

	private static final int HEIGHT = 600;
	private static final int WIDTH = 800;
	private static final String APPLICATION_NAME = "ほうとじつ - Music File Housekeeper";
	private static final String DEFAULT_FOLDER_LABEL = "No folder is selected";
	private static final String SEARCH = "Select Folder";
	private static final String START = "Start";
	private static final String READY = "Ready!";
	private static final String FOLDER_PATH = "Folder Path";
	
	private ProcessAudioFileUtil fileProcessor = new ProcessAudioFileUtil(this);
	private Mp3Processor mp3Processor = new Mp3Processor();
	private Mp4Processor mp4Processor = new Mp4Processor();
	private FlacProcessor flacProcessor = new FlacProcessor();
	
	private JFrame frame;
	private ImageIcon iconImage;
	
	private JPanel mainPane;
	private JPanel filePickerPane;
	private AboutPane aboutPane;
	private JScrollPane logPane;
	private JPanel footerPane;
	private BorderLayout mainPaneLayout;
	private BorderLayout filePickerLayout;
	private GridLayout footerPaneLayout;
	
	private JButton searchFolderButton;
	private JLabel filePathLabel;
	
	private JTextField filePathTextField;
	private JTextArea logTextPane;
	private JButton startButton;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmExit;
	
	private File selectedFolder;
	private JMenuItem mntmAbout;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainPane window = new MainPane();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public File getSelectedFolder() {
		return selectedFolder;
	}

	/**
	 * Create the application.
	 */
	public MainPane() {
		// build component
		filePickerPane();
		logPane();
		footerPane();
		menuBar();
		iconImage();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {		
		frame = new JFrame();
		frame.setTitle(APPLICATION_NAME);
		frame.setBounds((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2) - WIDTH/2, ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2) - HEIGHT/2, WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(iconImage.getImage());
		
		mainPane = new JPanel();
		mainPaneLayout = new BorderLayout();
		mainPaneLayout.setHgap(5);
		mainPaneLayout.setVgap(5);
		mainPane.setLayout(mainPaneLayout);
		mainPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		
		frame.getContentPane().add(mainPane);
		frame.setJMenuBar(menuBar);
		
		mainPane.add(filePickerPane, BorderLayout.NORTH);
		mainPane.add(logPane, BorderLayout.CENTER);
		mainPane.add(footerPane, BorderLayout.SOUTH);
	}
	
	private void iconImage() {
		iconImage = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/Sunflower-icon-24-24.png"));
	}
	
	private void menuBar() {
		menuBar = new JMenuBar();
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (aboutPane == null) {
					aboutPane = new AboutPane(frame);
				}
				aboutPane.setVisible(true);
			}
		});
		
		mnFile.add(mntmAbout);
		mnFile.add(mntmExit);
	}
	
	private void logPane() {
		logTextPane = new JTextArea();
		logTextPane.setLineWrap(true);
		logTextPane.setEditable(false);
		logTextPane.setText("== こんにちは ==");
		logTextPane.setMargin(new Insets(5, 5, 5, 5));
		logPane = new JScrollPane(logTextPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		logPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		logPane.setAutoscrolls(true);
		log(READY);
	}
	
	public void log(String log) {
		if (logTextPane != null) {
			logTextPane.setText(logTextPane.getText() + "\n" + log);
		}
	}
	
	private void footerPane() {
		footerPaneLayout = new GridLayout(1, 1);
		footerPaneLayout.setHgap(5);
		footerPaneLayout.setVgap(5);
		footerPane = new JPanel(footerPaneLayout);
		startButton = new JButton(START);
		startButton.setPreferredSize(new Dimension(WIDTH, 50));
		startButton.addActionListener(new OnStartButtonSelected());
		footerPane.add(startButton);
	}
	
	private void filePickerPane() {
		filePickerLayout = new BorderLayout(5, 5);
		
		filePickerPane = new JPanel(filePickerLayout);
		
		filePathLabel = new JLabel(FOLDER_PATH + " : ");
		filePathLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		filePathTextField = new JTextField(DEFAULT_FOLDER_LABEL);
		filePathTextField.setBackground(Color.WHITE);
		filePathTextField.setEditable(false);
		filePathTextField.setMargin(new Insets(0, 5, 0, 5));
		searchFolderButton = new JButton(SEARCH);
		
		filePickerPane.add(filePathLabel, BorderLayout.WEST);
		filePickerPane.add(filePathTextField, BorderLayout.CENTER);
		filePickerPane.add(searchFolderButton, BorderLayout.EAST);
		
		searchFolderButton.addActionListener(new OnSearchButtonSelected());
	}
	
	/**
	 * 
	 * @author joseph.tarigan@gmail.com
	 *
	 */
	private class OnStartButtonSelected implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			if (selectedFolder == null) {
				log("Folder hasn't been selected!");
				return;
			}
			
			File[] rawFileList = selectedFolder.listFiles();
			String fileType;
			
			for (File file : rawFileList) {
				if (file.isFile()) {
					try {
						fileType = Files.probeContentType(file.toPath());
						
						if (fileType.equalsIgnoreCase(MimeType.getMimeType(".mp3"))) {
							fileProcessor.setFileProcessor(mp3Processor);
						} else if (fileType.equalsIgnoreCase(MimeType.getMimeType(".mp4"))) {
							fileProcessor.setFileProcessor(mp4Processor);
						} else if (fileType.equalsIgnoreCase(MimeType.getMimeType(".flac"))) {
							fileProcessor.setFileProcessor(flacProcessor);
						}
						fileProcessor.process(file, selectedFolder);
					} catch (Exception e1) {
						log("- Error while proccessing " + file.getAbsolutePath());
					}
				}
			}
			
			log("\n\n== FINISHED ==\n\n");
		}
	}
	
	private class OnSearchButtonSelected implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fileChooser.showOpenDialog(frame);
		
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				selectedFolder = fileChooser.getSelectedFile();
				log("Opening " + selectedFolder.getAbsolutePath());
				filePathLabel.setText(selectedFolder.getAbsolutePath());
			}
		}		
	}
}
