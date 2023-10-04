import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.Random;

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
        rmoney.setBounds(10, 80, 250, 25);
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
            try (RandomAccessFile rom = new RandomAccessFile(file, "rw")) {
                byte[] romData = new byte[(int)rom.length()];
                rom.readFully(romData);

                // Perform randomization
                Random random = new Random();
                int randomGP = random.nextInt(501); // Generates a random number between 0 and 500

                // Modify the appropriate bytes in romData with the new randomGP value
                int offset1 = 0x752C9; // New address for starting gold (high byte)
                int offset2 = 0x752C4; // New address for starting gold (low byte)
                romData[offset1] = (byte)((randomGP >> 8) & 0xFF);
                romData[offset2] = (byte)(randomGP & 0xFF);

                // Save randomized ROM
                String randomizerDir = System.getProperty("user.dir");
                String randomizedRomPath = randomizerDir + File.separator + "randomized_rom.gbc";
                Files.write(Paths.get(randomizedRomPath), romData);

                // Additional code to display message dialog
                String message = "Randomized ROM saved as 'randomized_rom.gbc'\nNew GP value: " + randomGP;
                JOptionPane.showMessageDialog(null, message, "Randomized ROM Saved", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
