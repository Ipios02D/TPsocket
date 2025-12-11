package tp1;

import java.awt.*;
import java.awt.event.*;


public class Page1 extends Panel implements ActionListener {
	
    private Label prompt;
    private TextField pseudoField;
    private Button enterButton;

    private CardLayout cards;
    private Panel container;
    private Page2 page2;

    public Page1(CardLayout cards, Panel container, Page2 page2) {
        this.cards = cards;
        this.container = container;
        this.page2 = page2;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5,5));

        prompt = new Label("Veuillez entrez votre pseudo", Label.CENTER);
        add(prompt, BorderLayout.NORTH);

        Panel center = new Panel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        pseudoField = new TextField(20);
        enterButton = new Button("entrez");
        enterButton.addActionListener(this);

        center.add(pseudoField);
        center.add(enterButton);

        add(center, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enterButton) {
            String pseudo = pseudoField.getText();
            if (pseudo == null || pseudo.trim().length() == 0) {
                System.out.println("Pseudo vide : entrez un pseudo.");
                return;
            }

            cards.show(container, "page2");

            page2.displayMessage("=== " + pseudo + " a rejoint la discussion ===");
        }
    }
    
  
}
