package com.lgvb.valoui;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Store extends JFrame {

    private final java.util.List<Animal> animals = new ArrayList<>();
    private final java.util.List<Gun> guns = new ArrayList<>();

    private JPanel leftPanel, centerPanel, rightPanel, northPanel, southPanel;

    private final Color TRANSPARENT_GRAY = new Color(128, 128, 128, 180);
    private final Color DEFAULT_GRAY = new Color(0x73706c);
    private final Font DEFAULT_FONT = new Font("Tungsten", Font.BOLD, 15);

    public Store() {
        try {
            setTitle("Valo Store UI");
            setSize(2000, 1400);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(false);
            getContentPane().setBackground(Color.BLACK);
            setLayout(new BorderLayout(35, 0));

            loadAnimals();
            loadGuns();
            buildLayout();

            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error initializing Store UI: " + e.getMessage(),
                    "Initialization Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buildLayout() {
        try {
            leftPanel = createPanel(new Dimension(350, 0), Color.BLACK);
            centerPanel = createPanel(new Dimension(0, 0), Color.BLACK);
            rightPanel = createPanel(new Dimension(350, 0), DEFAULT_GRAY);
            northPanel = createPanel(new Dimension(0, 100), Color.BLACK);
            southPanel = createPanel(new Dimension(0, 100), Color.BLACK);

            add(leftPanel, BorderLayout.WEST);
            add(centerPanel, BorderLayout.CENTER);
            add(rightPanel, BorderLayout.EAST);
            add(northPanel, BorderLayout.NORTH);
            add(southPanel, BorderLayout.SOUTH);

            populateAnimals(leftPanel);
            populateGuns(centerPanel);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error building layout: " + e.getMessage(),
                    "Layout Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateAnimals(JPanel container) {
        try {
            for (Animal ani : animals) {
                JPanel panel = createPanel(new Dimension(330, 100), TRANSPARENT_GRAY);
                panel.setLayout(new BorderLayout());
                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));

                JLabel label = new JLabel(ani.getName());
                label.setFont(DEFAULT_FONT);
                label.setForeground(Color.GRAY);

                try {
                    ImageIcon icon = new ImageIcon(ani.getIcon().getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH));
                    label.setIcon(icon);
                } catch (Exception e) {
                    System.err.println("Error loading animal icon: " + ani.getName());
                }

                label.setVerticalTextPosition(JLabel.TOP);

                JLabel money = new JLabel("# " + ani.getMoney());
                money.setVerticalAlignment(JLabel.TOP);
                money.setFont(DEFAULT_FONT);
                money.setForeground(Color.GRAY);

                panel.add(label, BorderLayout.CENTER);
                panel.add(money, BorderLayout.EAST);

                container.add(panel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error populating animals: " + e.getMessage(),
                    "Animal Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateGuns(JPanel container) {
        container.removeAll();
        container.setLayout(new BorderLayout(10, 10));

        try {
            // ------------------- SIDEARMS (WEST) -------------------
            JPanel gunWestWrapper = new JPanel(new BorderLayout());
            gunWestWrapper.setBackground(Color.BLACK);

// small header at the top
            JPanel sidearmHeader = createLabeledPanel(new Dimension(150, 20), "sidearms");
            gunWestWrapper.add(sidearmHeader, BorderLayout.NORTH);

            // grid for guns (fills available space)
            JPanel gunWest = new JPanel(new GridLayout(0, 1, 5, 5));
            gunWest.setBackground(Color.BLACK);

            for (Gun gun : guns) {
                if (gun.getType().equalsIgnoreCase("sidearms")) {
                    gunWest.add(createGunPanel(new Dimension(150, 165), gun));
                }
            }

            // add grid directly (no scroll) so it stretches
            gunWestWrapper.add(gunWest, BorderLayout.CENTER);

            container.add(gunWestWrapper, BorderLayout.WEST);

            // ------------------- RIFLES (CENTER) -------------------
            JPanel gunCenter = createPanel(new Dimension(400, 0), Color.BLACK);
            gunCenter.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

            JPanel rifleHeader = createLabeledPanel(new Dimension(400, 20), "rifles");
            gunCenter.add(rifleHeader);

            for (Gun gun : guns) {
                if (gun.getType().equalsIgnoreCase("rifles")) {
                    gunCenter.add(createGunPanel(new Dimension(180, 200), gun));
                }
            }

            container.add(new JScrollPane(gunCenter), BorderLayout.CENTER);

            // ------------------- SMGS (EAST) -------------------
            JPanel gunEast = createPanel(new Dimension(250, 0), Color.BLACK);
            gunEast.setLayout(new BoxLayout(gunEast, BoxLayout.Y_AXIS));

            JPanel smgHeader = createLabeledPanel(new Dimension(250, 20), "smgs");
            gunEast.add(smgHeader);

            for (Gun gun : guns) {
                if (gun.getType().equalsIgnoreCase("smgs")) {
                    gunEast.add(createGunPanel(new Dimension(250, 120), gun));
                }
            }

            container.add(new JScrollPane(gunEast), BorderLayout.EAST);

            // ------------------- SNIPERS (SOUTH) -------------------
            JPanel gunSouth = createPanel(new Dimension(0, 200), Color.BLACK);
            gunSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

            JPanel sniperHeader = createLabeledPanel(new Dimension(400, 20), "snipers");
            gunSouth.add(sniperHeader);

            for (Gun gun : guns) {
                if (gun.getType().equalsIgnoreCase("snipers")) {
                    gunSouth.add(createGunPanel(new Dimension(180, 200), gun));
                }
            }

            container.add(new JScrollPane(gunSouth), BorderLayout.SOUTH);

            container.revalidate();
            container.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAnimals() {
        try (Scanner scanner = new Scanner(new FileReader("src/main/resources/animals.txt"))) {
            while (scanner.hasNextLine()) {
                try {
                    String[] data = scanner.nextLine().split(",");
                    animals.add(new Animal(
                            data[0].trim(), data[1].trim(), data[2].trim(),
                            Integer.parseInt(data[3].trim())
                    ));
                } catch (Exception e) {
                    System.err.println("Skipping invalid animal entry.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error in loading animals!");
            e.printStackTrace();
        }
    }

    private void loadGuns() {
        try (Scanner scanner = new Scanner(new FileReader("src/main/resources/guns.txt"))) {
            while (scanner.hasNextLine()) {
                try {
                    String[] data = scanner.nextLine().split(",");
                    guns.add(new Gun(
                            data[0].trim(), data[1].trim(), Integer.parseInt(data[2].trim())
                    ));
                } catch (Exception e) {
                    System.err.println("Skipping invalid gun entry.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error in loading guns!");
            e.printStackTrace();
        }
    }

    // ---------- panel helpers ----------
    private JPanel createPanel(Dimension dim, Color col) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(dim);
        panel.setBackground(col);
        return panel;
    }

    private JPanel createLabeledPanel(Dimension dim, String text) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(dim);
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(63, 99, 84, 150));
        panel.setBorder(BorderFactory.createLoweredBevelBorder());

        JLabel label = new JLabel(text.toUpperCase(), JLabel.CENTER);
        label.setFont(DEFAULT_FONT);
        label.setForeground(Color.WHITE);
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGunPanel(Dimension dim, Gun gun) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(dim);
        panel.setBackground(DEFAULT_GRAY);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            boolean clicked = false;

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                panel.setBackground(new Color(0x48be93));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                panel.setBackground(clicked ? new Color(32, 80, 57, 150) : DEFAULT_GRAY);
                panel.setBorder(clicked ? BorderFactory.createLineBorder(new Color(0x48be93), 3) : null);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                clicked = !clicked;
            }
        });

        JLabel label = new JLabel();
        try {
            label = new JLabel("# " + gun.getPrice() + " | " + gun.getName(), gun.getIcon(), JLabel.CENTER);
            ImageIcon icon = new ImageIcon(gun.getIcon().getImage().getScaledInstance(125, 80, Image.SCALE_SMOOTH));
            label.setIcon(icon);
        } catch (Exception e) {
            System.err.println("Error loading gun icon: " + gun.getName());
        }

        label.setFont(DEFAULT_FONT);
        label.setForeground(Color.LIGHT_GRAY);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.CENTER);

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
