package tmp.braingame.type;

import java.util.List;

import tmp.braingame.game.*;

public class GameQueue {
	private List<GameBaseScene> gamelist;
	private int count = 0;
	
	public GameQueue(List<GameBaseScene> pGameList){
		this.gamelist = pGameList;
	}
	
	public void loadAllResource(){
		for (GameBaseScene gameBaseScene : gamelist) {
			gameBaseScene.loadResouces();
		}
	}
	
	public boolean hasNext(){
		if(count < gamelist.size()) return true;
		return false;
	}
	
	public GameBaseScene getGame(){
		return gamelist.get(count++);
	}
}
