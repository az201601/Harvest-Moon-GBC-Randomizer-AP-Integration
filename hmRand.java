import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class hmRand {
	
	public static boolean randMoney = false;
	
    public static void main(String[] args) {
        // Create a JFrame (window)
        JFrame frame = new JFrame("Harvest Moon GBC Randomizer");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JPanel to hold the components
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        // Set the frame to be visible
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Create a label
        JLabel romLabel = new JLabel("Select ROM:");
        romLabel.setBounds(10, 20, 80, 25);
        panel.add(romLabel);

        // Create a label to display the ROM path
        JTextField romField = new JTextField();
        romField.setBounds(100, 20, 150, 25);
        panel.add(romField);

        // Create a button for browsing for the ROM
        JButton browseButton = new JButton("Browse");
        browseButton.setBounds(10, 50, 80, 25);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    romField.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        panel.add(browseButton);
		
		JCheckBox rmoney = new JCheckBox("Randomize the amount of Gold you start with");
		rmoney.setBounds(10, 80, 200, 25);
		panel.add(rmoney);
		rmoney.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent ie)
			{
				randMoney = ie.getStateChange() == ItemEvent.SELECTED;
			}
		});
		
		JButton randomizeButton = new JButton("Randomize!");
		randomizeButton.setBounds(10, 110, 100, 25);
		randomizeButton.addActionListener(new actionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				String romPath = romField.getText();
				File rom = new File(rompath);
				randomize(rom);
			}
		});
		panel.add(randomizerButton);
    }
	
	public static void randomize(File file)
	{
		if(randMoney)
		{
			try(RandomAccessFile rom = new RandomAccessFile(file, "rw")
			{
				rom.seek(0xB8EF);
				int currentValue = (rom.read() << 16) | (rom.read() << 8) | rom.read();
				Random random = new Random();
                int randomGP = random.nextInt(0x1F5);
				int newValue = (currentValue & 0xFF0000) | randomGP;
				rom.seek(0xB8EF);
                rom.write((newValue >> 16) & 0xFF);
                rom.write((newValue >> 8) & 0xFF);
                rom.write(newValue & 0xFF);
			}
			catch (IOException e) 
			{
                e.printStackTrace();
            }
			
			  String randomizerDir = System.getProperty("user.dir");
			  String randomizedRomPath = randomizerDir + File.separator + "randomized_rom.gbc";
              File randomizedRomFile = new File(randomizedRomPath);
			  try (RandomAccessFile randomizedRom = new RandomAccessFile(randomizedRomFile, "rw")) 
			  {
                    // Copy the content from original ROM to randomized ROM
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = rom.read(buffer)) != -1) 
					{
                        randomizedRom.write(buffer, 0, bytesRead);
					}
			 rom.close();
             randomizedRom.close();
			 }
			 catch (IOException e) 
			 {
				 e.printStackTrace();
			 }
		}
	}        
				
}
