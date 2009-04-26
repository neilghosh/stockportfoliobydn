package ng;

public class testmain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	/*	try {

			File file = new File("c:\\MyXMLFile.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db
					.parse("http://testjavaneil.appspot.com/stockprice?q=BSES");
			doc.getDocumentElement().normalize();

			NodeList nodeLst = doc.getElementsByTagName("price");
			Element priceElement = (Element) nodeLst.item(0);
			NodeList fstNm = priceElement.getChildNodes();
			System.out.println(((Node) fstNm.item(0)).getNodeValue());
			
		} catch (Exception e) {
			System.out.print("Ex..");
		}
*/
		
		
		System.out.print(ng.util.getName("BSES"));
		
		
		
		
		
		
		
	}
}
