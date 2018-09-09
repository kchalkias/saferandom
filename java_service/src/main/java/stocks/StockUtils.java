package stocks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import utils.Utils;
import utils.dbtools.DBDetails;
import utils.dbtools.DBPool;
import bitcoin.BlockData;

public class StockUtils {
	
	public static List<Stock> getNewStocks() throws IOException {
		URL url = new URL("http://www.barchart.com/stocks/sp500.php?_dtp1=0");
		Document doc = Jsoup.parse(url, 3000);

		Element table = doc.select("table[class=datatable ajax]").first();

		Iterator<Element> ite = table.select("tr").iterator();

		List<Stock> stocks = new ArrayList<Stock>();

		while (ite.hasNext()) {
			Elements elements = ite.next().getElementsByTag("td");
			String curSymbol = null;
			String curPrice = null;
			String curDate = null;
			if (elements.size() == 9) {
				for (int i = 0; i < elements.size(); i++) {
					if (elements.get(i).hasClass("ds_symbol")) {
						curSymbol = elements.get(i).text();
					} else if (elements.get(i).hasClass("ds_last")) {
						curPrice = elements.get(i).text();
					} else if (elements.get(i).hasClass("ds_displaytime")) {
						curDate = elements.get(i).text();
					}
				}
				stocks.add(new Stock(curSymbol, curPrice, curDate));
			}
		}
		return stocks;
	}
	
	public static void storeNewStocks() throws IOException {
		storeStocks(getNewStocks());
	}
	
	/*
	 * Store new blocks to our DB
	 */
	public static void storeStocks(List<Stock> newStocks) {
		if (newStocks == null) return;
		Connection c = null;
		java.sql.PreparedStatement pstmt = null;
		
		DBPool DB = DBPool.getInstance(DBDetails.SAFERANDOM);		
		try {
			c = DB.getConnection();
			if (c != null) {
				
				StringBuffer sf = new StringBuffer();
				for (Stock b : newStocks) {
					sf.append(b.getSymbol());
					sf.append(b.getPrice());
				}
				String value = sf.toString();
				pstmt = c.prepareStatement("INSERT IGNORE INTO snp (forday,value,seed) VALUES (?,?,?);");
				pstmt.setString(1, newStocks.get(0).getDay());
				pstmt.setString(2, value);
				pstmt.setString(3, Utils.SHA256(value));
				pstmt.addBatch();
				pstmt.executeBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBPool.closeEverything(pstmt, c);
		}
	}

}
