import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.Random;

public class Main{
    public static void main(String[] args){
        Window Game = new Window();
    }
}

class Buttons{
    boolean[][] ButtonState;
    JButton[][] Button;
    JPanel Panel;
    int ButtonAmount;
    int Clicks;
    JLabel ClicksLabel;
    public Buttons(int PositionX, int PositionY, JPanel TemporaryPanel, int TemporaryButtonAmount){
        Panel = TemporaryPanel;
        Clicks = 0;
        ClicksLabel = new JLabel(String.valueOf(Clicks));
        ClicksLabel.setBounds(PositionX, PositionY, 20, 20);
        Panel.add(ClicksLabel);
        ButtonAmount = TemporaryButtonAmount;
        Button = new JButton[ButtonAmount][ButtonAmount];
        ButtonState = new boolean[ButtonAmount][ButtonAmount];
        for (int j = 0; j < ButtonAmount; j++) {
            for (int i = 0; i < ButtonAmount; i++) {
                ButtonState[i][j] = false;
                int CurrentButtonX = i;
                int CurrentButtonY = j;
                Button[i][j] = new JButton();
                Button[i][j].setBackground(new Color(0x686868));
                Button[i][j].addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int ButtonX = CurrentButtonX;
                        int ButtonY = CurrentButtonY;
                        CorrectButtonPress(ButtonX, ButtonY);
                    }
                });
                Button[i][j].setBounds(PositionX+((300/ ButtonAmount)*i),
                        PositionY+((300/ ButtonAmount)*j)+20,
                        (300/ ButtonAmount), (300/ ButtonAmount));
                Panel.add(Button[i][j]);
            }
        }
    }
    public void ButtonPress(int x, int y){
        ButtonState[x][y] = !ButtonState[x][y];
        if (ButtonState[x][y]){
            Button[x][y].setBackground(new Color(0x249A11));
        } else Button[x][y].setBackground(new Color(0x686868));
    }
    public void CorrectButtonPress(int ButtonX, int ButtonY){
        ButtonPress(ButtonX, ButtonY);
        try {
            ButtonPress(ButtonX + 1, ButtonY);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            ButtonPress(ButtonX - 1, ButtonY);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            ButtonPress(ButtonX, ButtonY + 1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            ButtonPress(ButtonX, ButtonY - 1);
        } catch (IndexOutOfBoundsException ex){}
        Clicks++;
        ClicksLabel.setText(String.valueOf(Clicks));
        ClicksLabel.revalidate();
        ClicksLabel.repaint();
    }

    public void DeleteButtons(){
        for(int i = 0; i < ButtonAmount; i++) {
            for(int j = 0; j < ButtonAmount; j++) {
                Panel.remove(Button[i][j]);
            }
        }
        Panel.remove(ClicksLabel);
        Panel.revalidate();
        Panel.repaint();
    }

    public void Randomize(){
        Random Chance = new Random();
        for(int i = 0; i < ButtonAmount; i++){
            for(int j =0; j < ButtonAmount; j++){
                if(1 == Chance.nextInt(2)){
                    CorrectButtonPress(i, j);
                }
            }
        }
        Clicks = 0;
        ClicksLabel.setText(String.valueOf(Clicks));
    }
}

class Window{
    JButton Randomize;
    int ButtonAmount;
    JButton Settings;
    JButton Apply;
    Buttons Squares;
    JFrame Frame;
    JPanel ButtonPanel;
    JPanel TextPanel;
    JLayeredPane Layers;
    public Window(){
        Frame = new JFrame();
        Frame.setSize(600,600);
        Frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Layers = new JLayeredPane();
        Layers.setLayout(null);
        Frame.add(Layers);

        TextPanel = new JPanel();
        TextPanel.setBackground(new Color(0x98B893));
        TextPanel.setBounds(0, 0, 600, 600);
        TextPanel.setFocusable(true);
        Layers.add(TextPanel, JLayeredPane.PALETTE_LAYER);

        Text TextBlock = new Text(TextPanel, 48, 57);

        ButtonPanel = new JPanel(null);
        ButtonPanel.setBackground(new Color(0x454545));
        ButtonPanel.setBounds(0, 0, 600, 600);
        Layers.add(ButtonPanel, JLayeredPane.DEFAULT_LAYER);

        Squares = new Buttons(150, 150, ButtonPanel, ButtonAmount);

        Apply = new JButton("Apply");
        Apply.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean ValidInput = false;
                while(!ValidInput) {
                    try {
                        ButtonAmount = Integer.parseInt(TextBlock.getText());
                        ValidInput = true;
                    } catch (NumberFormatException _) {
                    }
                }
                Layers.setLayer(TextPanel, JLayeredPane.DEFAULT_LAYER);
                Layers.setLayer(ButtonPanel, JLayeredPane.PALETTE_LAYER);

                Squares.DeleteButtons();

                Squares = new Buttons(150, 150, ButtonPanel, ButtonAmount);
            }
        });
        TextPanel.add(Apply);

        Settings = new JButton("Set");
        Settings.setBounds(10, 10, 80, 80);
        Settings.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TextBlock.setText("");
                Layers.setLayer(TextPanel, JLayeredPane.PALETTE_LAYER);
                Layers.setLayer(ButtonPanel, JLayeredPane.DEFAULT_LAYER);
                TextPanel.requestFocusInWindow();
            }
        });
        ButtonPanel.add(Settings);

        Randomize = new JButton("rand");
        Randomize.setBounds(480, 10, 80, 80);
        Randomize.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Squares.Randomize();
            }
        });
        ButtonPanel.add(Randomize);

        Frame.setVisible(true);
    }
}
class Text{
    JLabel TextLabel;
    String TextInput;
    char Key;
    public Text(JPanel Back, int FirstKey, int LastKey){
        TextLabel = new JLabel();
        TextInput = "";
        Back.add(TextLabel);
        Back.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                Key = e.getKeyChar();
                if (Key == 8){
                    if (!Objects.equals(TextInput, "")){
                        TextInput = TextInput.substring(0, TextInput.length() - 1);
                    }
                }else if(Key >= FirstKey && Key <= LastKey){
                    TextInput = TextInput + Key;
                }
                TextLabel.setText(TextInput);
            };
        });
    }
    public String getText(){
        return TextInput;
    }
    public void setText(String x){
        TextInput = x;
        TextLabel.setText(x);
    }
}