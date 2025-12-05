public class ClientChat {

	public static void main(String[] args) {
		try {
			ServiceChat sc = (ServiceChat) Naming.lookup("ServicesObjetDistant");
			
			System.out.println("Client connecté au RMI");
			
			
			
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
