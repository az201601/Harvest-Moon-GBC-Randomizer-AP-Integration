import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

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

        JLabel romLabel = new JLabel("Select ROM:");
        romLabel.setBounds(10, 20, 80, 25);
        panel.add(romLabel);

        JTextField romField = new JTextField();
        romField.setBounds(100, 20, 150, 25);
        panel.add(romField);

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
        rmoney.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                randMoney = ie.getStateChange() == ItemEvent.SELECTED;
            }
        });

        JButton randomizeButton = new JButton("Randomize!");
        randomizeButton.setBounds(10, 110, 100, 25);
        randomizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String romPath = romField.getText();
                File rom = new File(romPath);
                randomize(rom);
            }
        });
        panel.add(randomizeButton);
    }

    public static void randomize(File file) {
        if (randMoney) {
            try (RandomAccessFile rom = new RandomAccessFile(file, "r")) {
                byte[] romData = new byte[(int)rom.length()];
                rom.readFully(romData);

                // Perform randomization
                // Modify romData as needed

                // Save randomized ROM
                String randomizerDir = System.getProperty("user.dir");
                String randomizedRomPath = randomizerDir + File.separator + "randomized_rom.gbc";
                Files.write(Paths.get(randomizedRomPath), romData);

                // Additional code to display message dialog
                String message = "Randomized ROM saved as 'randomized_rom.gbc'";
                JOptionPane.showMessageDialog(null, message, "Randomized ROM Saved", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
