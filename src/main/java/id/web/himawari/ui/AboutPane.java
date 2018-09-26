package id.web.himawari.ui;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @author joseph.tarigan@gmail.com
 *
 */
public class AboutPane extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextArea aboutContentArea;
	private JLabel iconImageLabel;
	private BorderLayout aboutContentLayout;
	private ImageIcon iconImage;
	
	public AboutPane(Frame owner) {
		super(owner, true);
		
		iconImage = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/Sunflower-icon.png"));
		iconImageLabel = new JLabel(iconImage);
		iconImageLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
		aboutContentLayout = new BorderLayout();
		
		aboutContentArea = new JTextArea();
		aboutContentArea.setLineWrap(true);
		aboutContentArea.setWrapStyleWord(true);
		aboutContentArea.setBackground(UIManager.getColor("Panel.background"));
		aboutContentArea.setEditable(false);
		aboutContentArea.setBorder(new EmptyBorder(15, 15, 15, 15));
		aboutContentArea.setText("It scans for mp3, mp4, and flac music files in the selected folder, and will organize those files according ot their Artist and Album information from their metadata.\n\nひまわり - \u00a9 joseph.tarigan@gmail.com - 2018");
		aboutContentArea.putClientProperty("html", null);
		
		getContentPane().setLayout(aboutContentLayout);
		getContentPane().add(iconImageLabel, BorderLayout.NORTH);
		getContentPane().add(new JLabel("ほうとじつ - Music File Housekeeper", JLabel.CENTER), BorderLayout.CENTER);
		getContentPane().add(aboutContentArea, BorderLayout.SOUTH);
		this.setTitle("About");
		this.setIconImage(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/Sunflower-icon-24-24.png")).getImage());
		this.setLocationRelativeTo(owner);
		this.setResizable(false);
		this.setSize(400, 300);
	}
}