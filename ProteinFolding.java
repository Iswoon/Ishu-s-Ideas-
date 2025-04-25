import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

enum SecondaryStructure {
    HELIX, SHEET, COIL
}

class AminoAcid {
    int x, y;
    char residue;
    Color color;
    SecondaryStructure structure;
    String fact;

    public AminoAcid(int x, int y, char residue, Color color, SecondaryStructure structure, String fact) {
        this.x = x;
        this.y = y;
        this.residue = residue;
        this.color = color;
        this.structure = structure;
        this.fact = fact;
    }

    public boolean containsPoint(int mx, int my) {
        return mx >= x && mx <= x + 30 && my >= y && my <= y + 30; //mx and my are the mouse coordinates
    }
}

class Protein {
    private String sequence;
    private AminoAcid[] chain;
    private Random random = new Random();

    public Protein(String sequence) {
        this.sequence = sequence;
        this.chain = new AminoAcid[sequence.length()];
        foldProtein();
    }

    public void foldProtein() {
        int legendOffset = 200;
        int panelWidth = 900;
        int aaSpacing = 35;
        int totalWidth = sequence.length() * aaSpacing;

        int availableWidth = panelWidth - legendOffset;
        int startX = legendOffset + (availableWidth - totalWidth) / 2;
        int startY = 200;

        for (int i = 0; i < sequence.length(); i++) {
            char aa = sequence.charAt(i);
            Color color = getAminoAcidColor(aa);

            SecondaryStructure structure = switch (i % 3) {
                case 0 -> SecondaryStructure.HELIX;
                case 1 -> SecondaryStructure.SHEET;
                default -> SecondaryStructure.COIL;
            };

            int offsetY = switch (structure) {
                case HELIX -> (int) (Math.sin(i * 0.5) * 40);
                case SHEET -> (i % 2 == 0) ? -20 : 20;
                case COIL -> (int) (Math.random() * 20 - 10);
            };

            String fact = getAminoAcidFact(aa);
            chain[i] = new AminoAcid(startX + (i * aaSpacing), startY + offsetY, aa, color, structure, fact);
        }
    }

    public void mutate() {
        int idx = random.nextInt(chain.length);
        char newAA = getRandomAminoAcid();
        chain[idx].residue = newAA;
        chain[idx].color = getAminoAcidColor(newAA);
        chain[idx].fact = getAminoAcidFact(newAA);
    }

    private char getRandomAminoAcid() {
        char[] aminoAcids = {'A', 'R', 'N', 'D', 'C', 'E', 'Q', 'G', 'H', 'I',
                'L', 'K', 'M', 'F', 'P', 'S', 'T', 'W', 'Y', 'V'};
        return aminoAcids[random.nextInt(aminoAcids.length)];
    }

    public void refold() {
        foldProtein();
    }

    public void denature() {
        for (int i = 0; i < chain.length; i++) {
            chain[i].x = 250 + (i * 35);
            chain[i].y = 200;
        }
    }

    public AminoAcid[] getChain() {
        return chain;
    }

    private Color getAminoAcidColor(char aa) {
        return switch (aa) {
            case 'A' -> new Color(255, 204, 204);
            case 'R' -> new Color(255, 153, 102);
            case 'N' -> new Color(204, 255, 153);
            case 'D' -> new Color(204, 204, 255);
            case 'C' -> new Color(204, 255, 204);
            case 'E' -> new Color(255, 255, 204);
            case 'Q' -> new Color(153, 204, 255);
            case 'G' -> new Color(153, 255, 255);
            case 'H' -> new Color(255, 204, 153);
            case 'I' -> new Color(204, 153, 255);
            case 'L' -> new Color(153, 255, 204);
            case 'K' -> new Color(255, 153, 153);
            case 'M' -> new Color(255, 204, 255);
            case 'F' -> new Color(255, 153, 204);
            case 'P' -> new Color(255, 153, 255);
            case 'S' -> new Color(153, 255, 153);
            case 'T' -> new Color(255, 229, 153);
            case 'W' -> new Color(204, 153, 153);
            case 'Y' -> new Color(255, 204, 102);
            case 'V' -> new Color(153, 204, 204);
            default -> Color.WHITE;
        };
    }

    private String getAminoAcidFact(char aa) {
        return switch (aa) {
            case 'A' -> "Alanine: Alanine is used to make glucose that is needed by the body!";
            case 'R' -> "Arginine: Arginine plays an important role in opening up the veins to enhance blood flow!";
            case 'N' -> "Asparagine: An amino acid that was discovered from asparagus!";
            case 'D' -> "Aspartic Acid: Aspartic acid is one of the amino acids that is most usable for energy!";
            case 'C' -> "Cysteine: Cysteine reduces the amount of black melanin pigmentation made!";
            case 'E' -> "Glutamic Acid: It is a non-essential amino acid and is the most important fuel for the gut!";
            case 'Q' -> "Glutamine: Glutamine protects the stomach and gastrointestinal tract!";
            case 'G' -> "Glycine: Glycine makes up one-third of collagen!";
            case 'H' -> "Histidine: An essential amino acid that is used to make histamine!";
            case 'I' -> "Isoleucine: Bulky and hydrophobic!";
            case 'L' -> "Leucine: Great for alpha helices!";
            case 'K' -> "Lysine: Essential amino acid! Foods such as bread and rice tend to be low in lysine!";
            case 'M' -> "Methionine: An essential amino acid which is the start of all proteins!";
            case 'F' -> "Phenylalanine: An essential amino acid used to make many types of useful amines.!";
            case 'P' -> "Proline: Kinky and rigid!";
            case 'S' -> "Serine: An amino acid used to make phospholipids and glyceric acid!";
            case 'T' -> "Threonine: An essential amino acid that is used to make the active site of enzymes!";
            case 'W' -> "Tryptophan: Aromatic and rare!";
            case 'Y' -> "Tyrosine: Tyrosine is used to make many types of useful amines!";
            case 'V' -> "Valine: Hydrophobic and stable!";
            default -> "Mystery amino acid!";
        };
    }
}

class ProteinVisualizer extends JPanel {
    private Protein protein;
    private String currentTooltip = "";
    private int mouseX = 0, mouseY = 0;

    public ProteinVisualizer(Protein protein) {
        this.protein = protein;
        setPreferredSize(new Dimension(900, 400));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                currentTooltip = "";

                for (AminoAcid aa : protein.getChain()) {
                    if (aa.containsPoint(mouseX, mouseY)) {
                        currentTooltip = aa.fact;
                        break;
                    }
                }

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawProtein(g);
        drawLegend(g);

        if (!currentTooltip.isEmpty()) {
            g.setColor(new Color(255, 255, 220));
            g.fillRoundRect(mouseX + 10, mouseY + 10, g.getFontMetrics().stringWidth(currentTooltip) + 10, 20, 10, 10);
            g.setColor(Color.BLACK);
            g.drawRoundRect(mouseX + 10, mouseY + 10, g.getFontMetrics().stringWidth(currentTooltip) + 10, 20, 10, 10);
            g.drawString(currentTooltip, mouseX + 15, mouseY + 25);
        }
    }

    private void drawBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(0, 0, new Color(216, 167, 177), 0, getHeight(), new Color(240, 240, 255));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawProtein(Graphics g) {
        for (AminoAcid aa : protein.getChain()) {
            g.setColor(aa.color);
            g.fillOval(aa.x, aa.y, 30, 30);
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(aa.residue), aa.x + 10, aa.y + 20);

            switch (aa.structure) {
                case HELIX -> g.drawString("H", aa.x + 12, aa.y - 5);
                case SHEET -> g.drawString("S", aa.x + 12, aa.y - 5);
                case COIL -> g.drawString("C", aa.x + 12, aa.y - 5);
            }
        }
    }

    private void drawLegend(Graphics g) {
        int x = 10;
        int y = 30;
        g.setColor(Color.BLACK);
        g.drawString("Amino Acids Legend:", x, y);
        y += 15;

        String[] aminoAcids = {"A: Alanine", "R: Arginine", "N: Asparagine", "D: Aspartic Acid", "C: Cysteine", "E: Glutamic Acid",
                "Q: Glutamine", "G: Glycine", "H: Histidine", "I: Isoleucine", "L: Leucine", "K: Lysine", "M: Methionine",
                "F: Phenylalanine", "P: Proline", "S: Serine", "T: Threonine", "W: Tryptophan", "Y: Tyrosine", "V: Valine"};

        for (String entry : aminoAcids) {
            g.drawString(entry, x, y);
            y += 15;
        }

        y += 20;

        g.setColor(Color.BLACK);
        g.drawString("Structure Key:", x, y);
        y += 15;
        g.drawString("H = Alpha Helix", x, y);
        y += 15;
        g.drawString("S = Beta Sheet", x, y);
        y += 15;
        g.drawString("C = Coil", x, y);
    }
}

public class ProteinFolding {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cute Protein Folding");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            Protein protein = new Protein("ACDEFGHIKLMNPQRSTVWY");
            ProteinVisualizer visualizer = new ProteinVisualizer(protein);
            frame.add(visualizer, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton mutateButton = new JButton("Mutate!");
            JButton refoldButton = new JButton("Refold!");
            JButton denatureButton = new JButton("Denature!");

            mutateButton.addActionListener(e -> {
                protein.mutate();
                visualizer.repaint();
            });

            refoldButton.addActionListener(e -> {
                protein.refold();
                visualizer.repaint();
            });

            denatureButton.addActionListener(e -> {
                protein.denature();
                visualizer.repaint();
            });

            buttonPanel.add(mutateButton);
            buttonPanel.add(refoldButton);
            buttonPanel.add(denatureButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);
        });
    }
}
