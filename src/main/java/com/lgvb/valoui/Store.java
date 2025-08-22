package com.lgvb.valoui;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Store extends JFrame {

    private final List<Animal> animals = new ArrayList<>();
    private final List<Gun> guns = new ArrayList<>();

    // root panels
    private JPanel westAnimals;   // root WEST (350, 0) -> animals only
    private JPanel eastInfo;      // root EAST (350, 0) -> info only
    private JPanel centerRoot;    // root CENTER -> holds main guns area (BorderLayout)
    private JPanel northSpacer;   // root NORTH (0, 100)
    private JPanel southSpacer;   // root SOUTH (0, 100)

    // styling
    private final Color TRANSPARENT_GRAY = new Color(128, 128, 128, 180);
    private final Color DEFAULT_GRAY = new Color(0x73706c);
    private final Color HEADER_BG = new Color(63, 99, 84, 150);
    private final Font DEFAULT_FONT = new Font("Tungsten", Font.BOLD, 15);

    // background image (optional)
    private Image bgImage = null;

    public Store() {
        try {
            setTitle("Valo Store UI");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(2000, 1400);
            setLocationRelativeTo(null);
            setResizable(false);

            // load background if present (non-fatal)
            try {
                bgImage = new ImageIcon("src/main/resources/firing range.png").getImage();
            } catch (Exception ignored) {
            }

            // set content pane to painter panel and root BorderLayout with horizontal gap preserved
            BackgroundPane bgPane = new BackgroundPane();
            setContentPane(bgPane);
            getContentPane().setLayout(new BorderLayout(35, 0)); // keep margins

            // load data
            loadAnimals();
            loadGuns();

            // build root layout with sizes enforced
            buildRootLayout();

            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error initializing Store UI: " + e.getMessage(),
                    "Initialization Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buildRootLayout() {
        // NORTH spacer (0, 100)
        northSpacer = createPanel(new Dimension(0, 100), new Color(0, 0, 0, 0));
        northSpacer.setOpaque(false);
        add(northSpacer, BorderLayout.NORTH);

        // SOUTH spacer (0, 100)
        southSpacer = createPanel(new Dimension(0, 100), new Color(0, 0, 0, 0));
        southSpacer.setOpaque(false);
        add(southSpacer, BorderLayout.SOUTH);

        // WEST: animals only (350, 0)
        westAnimals = createPanel(new Dimension(350, 0), new Color(0, 0, 0, 80));
        westAnimals.setLayout(new BoxLayout(westAnimals, BoxLayout.Y_AXIS));
        add(westAnimals, BorderLayout.WEST);
        populateAnimals(westAnimals);

        // EAST: info only (350, 0)
        eastInfo = createPanel(new Dimension(350, 0), new Color(0, 0, 0, 80));
        eastInfo.setLayout(new BorderLayout());
        add(eastInfo, BorderLayout.EAST);
        populateInfo(eastInfo);

        // CENTER: main guns area (we'll use BorderLayout)
        centerRoot = createPanel(new Dimension(0, 0), new Color(0, 0, 0, 0));
        centerRoot.setLayout(new BorderLayout(12, 12)); // small gaps inside center
        centerRoot.setOpaque(false);
        add(centerRoot, BorderLayout.CENTER);

        // Build center's internal layout (sidearms WEST, center columns CENTER (flow), armor EAST, abilities SOUTH)
        buildCenterGuns(centerRoot);
    }

    private void populateAnimals(JPanel container) {
        container.removeAll();
//        container.setOpaque(false);

        for (Animal ani : animals) {
            JPanel card = createPanel(new Dimension(330, 100), TRANSPARENT_GRAY);
            card.setLayout(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

            JLabel name = new JLabel(ani.getName());
            name.setFont(DEFAULT_FONT);
            name.setForeground(Color.WHITE);

            try {
                ImageIcon icon = new ImageIcon(ani.getIcon().getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH));
                name.setIcon(icon);
            } catch (Exception ignored) {
            }

            JLabel money = new JLabel("# " + ani.getMoney());
            money.setFont(DEFAULT_FONT);
            money.setForeground(Color.WHITE);

            card.add(name, BorderLayout.CENTER);
            card.add(money, BorderLayout.EAST);

            container.add(card);
            container.add(Box.createVerticalStrut(8));
        }
        container.revalidate();
        container.repaint();
    }

    private void populateInfo(JPanel container) {
        container.removeAll();
//        container.setOpaque(false);

        // Info header (keeps small)
        JPanel header = createHeaderPanel("INFO", 0, 28);
        container.add(header, BorderLayout.NORTH);

        // Info body as JTextArea (no HTML)
        JTextArea infoBody = new JTextArea();
        infoBody.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                + "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, "
                + "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        infoBody.setWrapStyleWord(true);
        infoBody.setLineWrap(true);
        infoBody.setEditable(false);
        infoBody.setOpaque(false);
        infoBody.setForeground(Color.WHITE);
        infoBody.setFont(new Font("SansSerif", Font.PLAIN, 13));
        infoBody.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane sp = new JScrollPane(infoBody);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(null);

        container.add(sp, BorderLayout.CENTER);
        container.revalidate();
        container.repaint();
    }

    private void buildCenterGuns(JPanel parent) {
        parent.removeAll();

        // CENTER WEST: sidearms panel (in centerRoot.WEST)
        JPanel sidearmsCol = createPanel(new Dimension(150, 0), new Color(0, 0, 0, 0));
        sidearmsCol.setLayout(new BorderLayout(0, 6));
        sidearmsCol.setOpaque(false);

        sidearmsCol.add(createHeaderPanel("SIDEARMS", 150, 22), BorderLayout.NORTH);

        JPanel sidearmsGrid = new JPanel(new GridLayout(0, 1, 8, 8));
        sidearmsGrid.setOpaque(false);
        sidearmsGrid.setBackground(new Color(0, 0, 0, 0));

        for (Gun g : guns) {
            if ("sidearms".equalsIgnoreCase(g.getType())) {
                sidearmsGrid.add(createGunCard(g, new Dimension(150, 120)));
            }
        }
        sidearmsCol.add(sidearmsGrid, BorderLayout.CENTER);
        parent.add(sidearmsCol, BorderLayout.WEST);

        // CENTER CENTER: 3 vertical columns side-by-side
        JPanel centerColumns = new JPanel(new GridLayout(1, 3, 30, 0)); // 1 row, 3 columns, 30px gap
        centerColumns.setOpaque(false);

// Column 1: SMGs + Shotguns
        JPanel col1 = createColumnPanel();
        col1.add(createCategoryPanel("SMGS", "smgs"));
        col1.add(Box.createVerticalStrut(12));
        col1.add(createCategoryPanel("SHOTGUNS", "shotguns"));

// Column 2: Rifles
        JPanel col2 = createColumnPanel();
        col2.add(createCategoryPanel("RIFLES", "rifles"));

// Column 3: Snipers + Machine Guns
        JPanel col3 = createColumnPanel();
        col3.add(createCategoryPanel("SNIPER RIFLES", "sniper_rifles"));
        col3.add(Box.createVerticalStrut(12));
        col3.add(createCategoryPanel("MACHINE GUNS", "machine_guns"));

        centerColumns.add(col1);
        centerColumns.add(col2);
        centerColumns.add(col3);

        parent.add(centerColumns, BorderLayout.CENTER);

        // CENTER EAST: Armor column (inside center)
        JPanel armorCol = createPanel(new Dimension(150, 0), new Color(0, 0, 0, 0));
        armorCol.setLayout(new BorderLayout(0, 6));
        armorCol.setOpaque(false);
        armorCol.add(createHeaderPanel("ARMOR", 150, 22), BorderLayout.NORTH);

        JPanel armorGrid = new JPanel(new GridLayout(0, 1, 8, 8));
        armorGrid.setOpaque(false);
        for (Gun g : guns) {
            if ("armor".equalsIgnoreCase(g.getType())) {
                armorGrid.add(createGunCard(g, new Dimension(150, 120)));
            }
        }
        armorCol.add(armorGrid, BorderLayout.CENTER);
        parent.add(armorCol, BorderLayout.EAST);

        // CENTER SOUTH: Abilities row
        JPanel abilitiesRowWrap = createPanel(new Dimension(0, 150), new Color(0, 0, 0, 0));
        abilitiesRowWrap.setLayout(new BorderLayout());
        abilitiesRowWrap.setOpaque(false);

// small header
        abilitiesRowWrap.add(createHeaderPanel("ABILITIES", 0, 22), BorderLayout.NORTH);

// abilities fill the rest with equal width
        JPanel abilitiesRow = new JPanel(new GridLayout(1, 0, 12, 0)); // 1 row, stretch horizontally
        abilitiesRow.setOpaque(false);

        abilitiesRow.add(createAbilityCard("# 200 | Trademark", "src/main/resources/abilities/trademark.png", null));
        abilitiesRow.add(createAbilityCard("# 100 | Headhunter", "src/main/resources/abilities/headhunter.png", null));
        abilitiesRow.add(createAbilityCard("# 0 | Rendezvous", "src/main/resources/abilities/rendezvous.png", null));

        abilitiesRowWrap.add(abilitiesRow, BorderLayout.CENTER);
        parent.add(abilitiesRowWrap, BorderLayout.SOUTH);

        parent.revalidate();
        parent.repaint();
    }

    // ---------- small UI helpers ----------
    private JPanel createPanel(Dimension pref, Color bg) {
        JPanel p = new JPanel();
        if (pref != null) {
            p.setPreferredSize(pref);
        }
        p.setBackground(bg);
        p.setOpaque(bg.getAlpha() != 0);
        return p;
    }

    private JPanel createHeaderPanel(String title, int widthHint, int height) {
        JPanel header = new JPanel(new BorderLayout());
        if (widthHint > 0) {
            header.setPreferredSize(new Dimension(widthHint, height));
        }
        header.setBackground(HEADER_BG);
        header.setBorder(BorderFactory.createLoweredBevelBorder());
        JLabel lbl = new JLabel(title.toUpperCase(), JLabel.CENTER);
        lbl.setFont(DEFAULT_FONT);
        lbl.setForeground(Color.WHITE);
        header.add(lbl, BorderLayout.CENTER);
        return header;
    }

    private JPanel createColumnPanel() {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);
        return col;
    }

    private JPanel createCategoryPanel(String title, String typeKey) {
        // give each category a usable width (e.g., 300px)
        JPanel box = createPanel(new Dimension(300, 320), DEFAULT_GRAY);
        box.setLayout(new BorderLayout(8, 8));

        box.add(createHeaderPanel(title, 0, 22), BorderLayout.NORTH);

        // inside createCategoryPanel
        JPanel grid = new JPanel(new GridLayout(0, 1, 8, 8)); // 1 column, vertical stack
        grid.setOpaque(false);

        for (Gun g : guns) {
            if (typeKey.equalsIgnoreCase(g.getType())) {
                grid.add(createGunCard(g, new Dimension(250, 120))); // height can be smaller
            }
        }
        box.add(grid, BorderLayout.CENTER);

        return box;
    }

    private JPanel createGunCard(Gun g, Dimension pref) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(DEFAULT_GRAY);
        if (pref != null) {
            card.setPreferredSize(pref);
        }

        JLabel lbl;
        try {
            ImageIcon icon = g.getIcon();
            if (icon != null) {
                Image scaled = icon.getImage().getScaledInstance(125, 80, Image.SCALE_SMOOTH);
                lbl = new JLabel("# " + g.getPrice() + " | " + g.getName(), new ImageIcon(scaled), JLabel.CENTER);
            } else {
                lbl = new JLabel("# " + g.getPrice() + " | " + g.getName(), JLabel.CENTER);
            }
        } catch (Exception ex) {
            lbl = new JLabel("# " + g.getPrice() + " | " + g.getName(), JLabel.CENTER);
        }
        lbl.setFont(DEFAULT_FONT);
        lbl.setForeground(Color.LIGHT_GRAY);
        lbl.setVerticalTextPosition(SwingConstants.BOTTOM);
        lbl.setHorizontalTextPosition(SwingConstants.CENTER);
        card.add(lbl, BorderLayout.CENTER);

        // mouse hover / click behaviour
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            boolean clicked = false;

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(0x48be93));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(clicked ? new Color(32, 80, 57, 150) : DEFAULT_GRAY);
                card.setBorder(clicked ? BorderFactory.createLineBorder(new Color(0x48be93), 3) : null);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                clicked = !clicked;
            }
        });

        return card;
    }

    // Overloaded ability-card helpers:
    // 2-arg convenience method (keeps old call-sites safe)
    private JPanel createAbilityCard(String text, String iconPath) {
        return createAbilityCard(text, iconPath, new Dimension(200, 100));
    }

    // 3-arg method (custom size)
    private JPanel createAbilityCard(String text, String iconPath, Dimension size) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(DEFAULT_GRAY);
        if (size != null) {
            card.setPreferredSize(size); // only enforce if provided
        }
        JLabel lbl = new JLabel(text, JLabel.CENTER);
        lbl.setFont(DEFAULT_FONT);
        lbl.setForeground(Color.LIGHT_GRAY);
        lbl.setVerticalTextPosition(SwingConstants.BOTTOM);
        lbl.setHorizontalTextPosition(SwingConstants.CENTER);

        try {
            ImageIcon ii = new ImageIcon(iconPath);
            Image scaled = ii.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(scaled));
        } catch (Exception ignored) {
        }

        card.add(lbl, BorderLayout.CENTER);
        return card;
    }

    // ---------- data loaders ----------
    private void loadAnimals() {
        try (Scanner sc = new Scanner(new FileReader("src/main/resources/animals.txt"))) {
            while (sc.hasNextLine()) {
                try {
                    String[] parts = sc.nextLine().split(",");
                    animals.add(new Animal(parts[0].trim(), parts[1].trim(), parts[2].trim(), Integer.parseInt(parts[3].trim())));
                } catch (Exception ignored) {
                    System.err.println("Skipping invalid animal line.");
                }
            }
        } catch (IOException e) {
            System.err.println("animals.txt load error: " + e.getMessage());
        }
    }

    private void loadGuns() {
        try (Scanner sc = new Scanner(new FileReader("src/main/resources/guns.txt"))) {
            while (sc.hasNextLine()) {
                try {
                    String[] parts = sc.nextLine().split(",");
                    guns.add(new Gun(parts[0].trim(), parts[1].trim(), Integer.parseInt(parts[2].trim())));
                } catch (Exception ignored) {
                    System.err.println("Skipping invalid gun line.");
                }
            }
        } catch (IOException e) {
            System.err.println("guns.txt load error: " + e.getMessage());
        }
    }

    // ---------- Background painter ----------
    private class BackgroundPane extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                // scale to fill while preserving aspect ratio
                int w = getWidth(), h = getHeight();
                int iw = bgImage.getWidth(null), ih = bgImage.getHeight(null);
                if (iw > 0 && ih > 0) {
                    double scale = Math.max(w / (double) iw, h / (double) ih);
                    int drawW = (int) (iw * scale), drawH = (int) (ih * scale);
                    int x = (w - drawW) / 2, y = (h - drawH) / 2;
                    g.drawImage(bgImage, x, y, drawW, drawH, this);
                } else {
                    g.drawImage(bgImage, 0, 0, w, h, this);
                }
            } else {
                setBackground(Color.DARK_GRAY);
            }
        }
    }

    // ---------- main for quick test ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Store());
    }
}
