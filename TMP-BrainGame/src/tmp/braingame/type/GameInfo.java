package tmp.braingame.type;

public class GameInfo {
	public GameInfo(String pName, String pCatalog,
			String pClassPath) {
		this.Name = pName;
		this.ClassPath = pClassPath;
		this.Catalog = pCatalog;
	}
	public String Catalog;
	public String ClassPath;
	public String Name;
}
