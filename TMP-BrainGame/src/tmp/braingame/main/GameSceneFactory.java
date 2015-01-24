package tmp.braingame.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tmp.braingame.R;
import tmp.braingame.game.GameBaseScene;
import tmp.braingame.type.GameInfo;
import tmp.braingame.type.GameQueue;

public class GameSceneFactory {
	private final static GameSceneFactory INSTANCE = new GameSceneFactory();

	public static GameSceneFactory getInstance() {
		return INSTANCE;
	}

	private final String DLC_CONFIG_URL = "GameDeclare.xml";

	private HashMap<String, List<GameInfo>> gameManager;

	public void loadData() {
		gameManager = new HashMap<String, List<GameInfo>>();

		try {
			InputStream databaseInputStream = ResourcesManager.getInstance().gameActivity
					.getResources().openRawResource(R.raw.gamedeclare);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(databaseInputStream);
			doc.getDocumentElement().normalize();

			NodeList gameList = doc.getElementsByTagName("Game");

			for (int i = 0; i < gameList.getLength(); i++) {
				Node node = gameList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) node;

					GameInfo game = new GameInfo(eElement
							.getElementsByTagName("Name").item(0)
							.getTextContent(), eElement
							.getElementsByTagName("Catalog").item(0)
							.getTextContent(), eElement
							.getElementsByTagName("ClassPath").item(0)
							.getTextContent());

					if (!gameManager.containsKey(game.Catalog)) {
						gameManager
								.put(game.Catalog, new ArrayList<GameInfo>());
					}
					gameManager.get(game.Catalog).add(game);
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public GameQueue getSpecificGame(String pCatalog, String pName) {
		List<GameInfo> gameList = gameManager.get(pCatalog);
		System.out.print(gameList.size());
		for (GameInfo gameInfo : gameList) {
			if (gameInfo.Name.equals(pName) ) {
				try {
					// Class<?> clazz = Class.forName(gameInfo.ClassPath);
					// Constructor<?> ctor = clazz.getConstructor(String.class);
					// GameBaseScene game = (GameBaseScene) ctor.newInstance(new
					// Object[]{});

					Class clzz = Class.forName(gameInfo.ClassPath);
					GameBaseScene game = (GameBaseScene) clzz.newInstance();
					List<GameBaseScene> list = new ArrayList<GameBaseScene>();
					list.add(game);
					return new GameQueue(list);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
