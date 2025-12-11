import java.awt.*;
import java.awt.event.*;


public class Page2 extends Panel implements ActionListener {
    private TextArea messageArea;
    private TextField inputField;
    private Button sendButton;
    private Button outButton;

    private CardLayout cards;
    private Panel container;

    public interface MessageListener {
        void onMessageSent(String message);
    }
    private MessageListener messageListener;

    public Page2(CardLayout cards, Panel container) {
        this.cards = cards;
        this.container = container;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        Panel topBar = new Panel(new BorderLayout());
        Panel topRight = new Panel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        outButton = new Button("out");
        outButton.addActionListener(this);
        topRight.add(outButton);
        topBar.add(topRight, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);
        
        messageArea = new TextArea("", 15, 60, TextArea.SCROLLBARS_VERTICAL_ONLY);
        messageArea.setEditable(false);
        add(messageArea, BorderLayout.CENTER);

        Panel bottom = new Panel(new BorderLayout(5, 5));
        inputField = new TextField();
        sendButton = new Button("envoyez");
        sendButton.addActionListener(this);

        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
    }

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == sendButton) {
            String text = inputField.getText();
            if (text != null && text.trim().length() > 0) {
                if (messageListener != null) {
                    messageListener.onMessageSent(text);
                }
                
                inputField.setText("");
            }
        } else if (src == outButton) {
            cards.show(container, "page1");
        }
    }

    
    public void displayMessage(String msg) {
        if (msg == null) return;
        messageArea.append(msg + "\n");
        messageArea.setCaretPosition(messageArea.getText().length());
    }
}

