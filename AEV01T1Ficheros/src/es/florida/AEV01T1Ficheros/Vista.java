package es.florida.AEV01T1Ficheros;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputMethodListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.InputMethodEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class Vista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField searchBarField;
	private JTextField searchMatchesBar;
	private JTextField replaceMatchesBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Vista frame = new Vista();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Vista() {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 895, 565);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 859, 504);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 11, 839, 166);
		panel.add(scrollPane_1);
		
		JTextArea textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 190, 839, 23);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		searchBarField = new JTextField();
		searchBarField.setToolTipText("select a folder or insert a path to explore");
		searchBarField.setBounds(0, 1, 610, 20);
		panel_1.add(searchBarField);
		searchBarField.addInputMethodListener(new InputMethodListener() {
			public void caretPositionChanged(InputMethodEvent event) {
			}
			public void inputMethodTextChanged(InputMethodEvent event) {
				
			}
		});
		searchBarField.setColumns(10);
		
		JButton fileExplorerBtn = new JButton("File Explorer");
		fileExplorerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            int status = chooser.showOpenDialog(null);
	            if (status == JFileChooser.APPROVE_OPTION) {
	                File file = chooser.getSelectedFile();
	                if (file == null) {
	                    return;
	                }

	                String fileName = chooser.getSelectedFile().getAbsolutePath();
	                searchBarField.setText(fileName);
	                searchMatchesBar.setEnabled(false);
	            }

			}
		});
		fileExplorerBtn.setBounds(620, 0, 108, 23);
		panel_1.add(fileExplorerBtn);
		
		JButton searchBtn = new JButton("Search");
		searchBtn.setBounds(738, 0, 101, 23);
		panel_1.add(searchBtn);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 269, 839, 122);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JCheckBox caseSensitiveCheckbox = new JCheckBox("Case sensitive");
		caseSensitiveCheckbox.setBounds(10, 28, 110, 23);
		panel_2.add(caseSensitiveCheckbox);
		
		searchMatchesBar = new JTextField();
		searchMatchesBar.setToolTipText("insert a word to find");
		searchMatchesBar.setEnabled(false);
		searchMatchesBar.addInputMethodListener(new InputMethodListener() {
			public void caretPositionChanged(InputMethodEvent event) {
			}
			public void inputMethodTextChanged(InputMethodEvent event) {
			}
		});
		searchMatchesBar.setBounds(0, 1, 610, 20);
		panel_2.add(searchMatchesBar);
		searchMatchesBar.setColumns(10);
		JCheckBox accentCheckbox = new JCheckBox("Accent sensitive");
		accentCheckbox.setSelected(true);
		accentCheckbox.setBounds(122, 28, 130, 23);
		panel_2.add(accentCheckbox);
		JButton searchMatchesBtn = new JButton("Search Matches");
		searchMatchesBtn.setEnabled(false);
		searchMatchesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	
				String matches = "";
				
				boolean checkboxSelected = caseSensitiveCheckbox.isSelected();
				boolean accentCheckboxSelected = accentCheckbox.isSelected();
								
				File pathToFileSearchMatches = new File(searchBarField.getText());
				
				String matchesToSearch = searchMatchesBar.getText();
				
				if(checkboxSelected) {

					try {
						matches = Controlador.getSubPaths(pathToFileSearchMatches, matchesToSearch, checkboxSelected, false, null, accentCheckboxSelected);
					} catch (IOException e1) {
						e1.printStackTrace();
						e1.getMessage();
					}
	
				}else {
					
					try {
						matches = Controlador.getSubPaths(pathToFileSearchMatches, matchesToSearch, checkboxSelected, false, null, accentCheckboxSelected);
					} catch (IOException e1) {
						e1.printStackTrace();
						e1.getMessage();
					}
					
				}
				textArea.setText(matches);
				JOptionPane.showMessageDialog(null, matches, "Search matches ", JOptionPane.INFORMATION_MESSAGE);
			
			}
		});
		
		searchMatchesBtn.setBounds(621, 0, 218, 23);
		panel_2.add(searchMatchesBtn);
		
		
		
		
		replaceMatchesBar = new JTextField();
		replaceMatchesBar.setToolTipText("insert the word to be substituted");
		replaceMatchesBar.setEnabled(false);
		replaceMatchesBar.setBounds(0, 70, 610, 20);
		panel_2.add(replaceMatchesBar);
		replaceMatchesBar.setColumns(10);
		
		JButton replaceBtn = new JButton("Replace");
		replaceBtn.setEnabled(false);
		replaceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String matches = "";
				boolean checkboxSelected = caseSensitiveCheckbox.isSelected();
				boolean accentCheckboxSelected = accentCheckbox.isSelected();
				
				File pathToFileSearchMatches = new File(searchBarField.getText());
				
				String matchesToSearch = searchMatchesBar.getText();
				String matchesToReplace = replaceMatchesBar.getText();

				try {
					matches = Controlador.getSubPaths(pathToFileSearchMatches, matchesToSearch, checkboxSelected, true, matchesToReplace, accentCheckboxSelected);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				textArea.setText(matches);
				JOptionPane.showMessageDialog(null, matches, "Search matches ", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		replaceBtn.setBounds(621, 69, 218, 23);
		panel_2.add(replaceBtn);
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String searchBarFieldContent = searchBarField.getText();
				
				String pathInfo = Controlador.getDirectoryListing(searchBarFieldContent);
				textArea.setText(pathInfo);
				searchMatchesBar.setEnabled(true);
				replaceMatchesBar.setEnabled(true);
				
				replaceBtn.setEnabled(true);
				searchMatchesBtn.setEnabled(true);
			
			}
		});
	}
}
