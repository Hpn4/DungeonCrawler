package loader.generator;

import java.io.File;
import java.util.ArrayList;

import loader.map.Map;

public class TwoDoorRoomMaker extends AbstractRoomMaker {
	
	// Passage horizontal (gauche droite)
	private final static ArrayList<String> passageGD;
	
	// Passage angle montant (gauche haut)
	private final static ArrayList<String> passageGH;
	
	// Passage angle descendant (gauche bas)
	private final static ArrayList<String> passageGB;
	
	// Passage vetical (haut bas)
	private final static ArrayList<String> passageHB;
	
	private final static String path = "templates/pattern/2/";
	
	static {
		passageGD = new ArrayList<>();
		passageGH = new ArrayList<>();
		passageGB = new ArrayList<>();
		passageHB = new ArrayList<>();
		
		addAllPassageFrom(passageGD, "gd/");
		addAllPassageFrom(passageGH, "gh/");
		addAllPassageFrom(passageGB, "gb/");
		addAllPassageFrom(passageHB, "hb/");
	}
	
	private static void addAllPassageFrom(final ArrayList<String> list, final String p) {
		final String[] files = new File(path + p).list();
		
		for(final String file: files)
			if(file.startsWith("passage_"))
				list.add(file);
	}
	
	public TwoDoorRoomMaker(final boolean doorO, final boolean doorN, final boolean doorE, final boolean doorS) throws Exception {
		if(doorO && doorE)
			map = getRandomFrom(passageGD, path + "gd/");
		else if(doorO && doorN)
			map = getRandomFrom(passageGH, path + "gh/");
		else if(doorO && doorS)
			map = getRandomFrom(passageGB, path + "gb/");
		else if(doorN && doorS)
			map = getRandomFrom(passageHB, path + "hb/");
		else
			map = new Map("templates/pattern/vide.tmx");
	}
}
