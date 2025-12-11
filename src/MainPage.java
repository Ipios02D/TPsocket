import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainPage {

	  public static void main(String[] args) {
	        Frame frame = new Frame("DevWeb TP1 Chat");
	        frame.setSize(600, 420);
	        frame.setLayout(new BorderLayout());
	        CardLayout cards = new CardLayout();
	        Panel container = new Panel(cards);


	        Page2 page2 = new Page2(cards, container);
	        Page1 page1 = new Page1(cards, container, page2);

	        
	        container.add(page1, "page1");
	        container.add(page2, "page2");

	        cards.show(container, "page1");

	        frame.add(container, BorderLayout.CENTER);


	        frame.addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent e) {
	                frame.dispose();
	                System.exit(0);
	            }
	        });

	        frame.setVisible(true);
	    }
	
}

