package server.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import server.communication.Server;
import shared.communication.IServer;
import shared.definitions.DevCardType;
import shared.definitions.MunicipalityType;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.DevCardList;
import shared.model.ModelFacade;
import shared.model.Municipality;
import shared.model.Player;
import shared.model.PlayerReference;
import shared.model.Port;
import shared.model.ResourceList;
import shared.model.ResourceTradeList;
import shared.model.TradeOffer;

public class SteveAI extends AIPlayer{

	private boolean soldier = false;
	private Random random;
	private IServer server = Server.getSingleton();
	
	public SteveAI(ModelFacade game, Player player) {
		super(game, player);
		random = new Random();
	}

	@Override
	public void firstRound() {
		while(true){
			int x = random.nextInt(5) - 2;
			int y = random.nextInt(5) - 2;
			int rx = x;
			int ry = y;
			int directionSettlement = random.nextInt(6);
			int directionRoad = random.nextInt(3);
			
			VertexDirection dir = selectVertex(directionSettlement);
			EdgeDirection edir;
						
			switch (directionRoad){
			case 0:
				edir = EdgeDirection.North;
				if(dir.equals(VertexDirection.East)){
					rx = rx + 1;
				}
				else if(dir.equals(VertexDirection.West)){
					rx = rx - 1;
					ry = ry + 1;
				}
				break;
			case 1:
				edir = EdgeDirection.NorthEast;
				if(dir.equals(VertexDirection.NorthWest)){
					rx = rx - 1;
				}
				else if(dir.equals(VertexDirection.SouthEast)){
					ry = ry + 1;
				}
				break;
			case 2:
				edir = EdgeDirection.NorthWest;
				if(dir.equals(VertexDirection.NorthEast)){
					rx = rx + 1;
					ry = ry - 1;
				}
				else if(dir.equals(VertexDirection.SouthWest)){
					ry = ry + 1;
				}
				break;
			default:
				return;
			}
			
			HexLocation hex = new HexLocation(x, y);
			HexLocation rhex1 = new HexLocation(rx, ry);
			
			VertexLocation settlement = new VertexLocation(hex, dir);
			EdgeLocation road = new EdgeLocation(rhex1, edir);
			
			if(super.game.canBuildStartingPieces(settlement, road)){
				try{
					server.buildStartingPieces(player.getUUID(), game.getUUID(), settlement, road);
					return;
				}
				catch(Exception e){
					System.out.println("Server Error: Could not build starting piece");
				}
			}
		}
	}

	@Override
	public void secondRound() {
		firstRound();
	}

	@Override
	public void takeTurn(){
		boolean trade = true;
		while(true){
			ResourceList resources = player.getResources();
			DevCardList devCards = player.getOldDevCards();
			List<String> actions = new ArrayList<String>();
			int points = player.getVictoryPoints();
			int monuments = devCards.count(DevCardType.MONUMENT);
			if(points + monuments >= 10){
				for(int i = 0; i < monuments; ++i){
					try{
						server.monument(player.getUUID(), game.getUUID());
					}
					catch(Exception e){
						System.out.println("Server Problem: Could not play monument card");
					}
				}
			}
			
			if(player.canBuyDevCard()){
				actions.add("BuyDevCard");
			}
			if(devCards.count(DevCardType.YEAR_OF_PLENTY) > 0){
				actions.add("YearOfPlenty");
			}
			if(devCards.count(DevCardType.SOLDIER) > 0){
				actions.add("Soldier");
			}
			if(devCards.count(DevCardType.MONOPOLY) > 0){
				actions.add("Monopoly");
			}
			if(devCards.count(DevCardType.ROAD_BUILD) > 0){
				actions.add("RoadBuilding");
			}
			if(player.canBuildRoad()){
				actions.add("BuildRoad");
			}
			if(player.canBuildSettlement()){
				actions.add("BuildSettlement");
			}
			if(player.canBuildCity()){
				actions.add("BuildCity");
			}
			if(resources.count() > 0 && trade){
				actions.add("OfferTrade");
			}
			for(ResourceType type : ResourceType.values()){
				int count = resources.count(type);
				int ratio = getRatio(type);
				
				if(count >= ratio){
					switch (type){
					case BRICK:
						actions.add("MaritimeTradeBrick");
						break;
					case WOOD:
						actions.add("MaritimeTradeWood");
						break;
					case SHEEP:
						actions.add("MaritimeTradeSheep");
						break;
					case ORE:
						actions.add("MaritimeTradeOre");
						break;
					case WHEAT:
						actions.add("MaritimeTradeWheat");
						break;
					}
				}
			}
			
			int choice = random.nextInt(actions.size() + 1);
			System.out.println("Size: " + actions.size());
			System.out.println("Choice: " + choice);
			if(choice == actions.size()){
				return;
			}
			
			switch (actions.get(choice)){
			case "BuyDevCard":
				try{
					server.buyDevCard(player.getUUID(), game.getUUID());
				}
				catch(Exception e){
					System.out.println("Server Error: could not buy Dev Card");
				}
				break;
			case "YearOfPlenty":
				yearOfPlenty();
				break;
			case "Soldier":
				soldier = true;
				robber();
				break;
			case "Monopoly":
				monopoly();
				break;
			case "RoadBuilding":
				roadBuilding();
				break;
			case "BuildRoad":
				try{
					EdgeLocation road = buildRoad();
					if(road != null){
						server.buildRoad(player.getUUID(), game.getUUID(), buildRoad());
					}
				}
				catch(Exception e){
					System.out.println("Server Error: Could not build road");
				}
				break;
			case "BuildSettlement":
				buildSettlement();
				break;
			case "BuildCity":
				buildCity();
				break;
			case "OfferTrade":
				trade = false;
				offerTrade();
				break;
			case "MaritimeTradeBrick":
				maritimeTrade(ResourceType.BRICK);
				break;
			case "MaritimeTradeWood":
				maritimeTrade(ResourceType.WOOD);
				break;
			case "MaritimeTradeWheat":
				maritimeTrade(ResourceType.WHEAT);
				break;
			case "MaritimeTradeOre":
				maritimeTrade(ResourceType.ORE);
				break;
			case "MaritimeTradeSheep":
				maritimeTrade(ResourceType.SHEEP);
				break;
			}
		}
	}
	
	@Override
	public boolean tradeOffered(TradeOffer offer){
		if(!game.canAcceptTrade()){
			return false;
		}
		int number = random.nextInt(2);
		if(number == 0){
			return true;
		}
		return false;
	}
	
	@Override
	public void discard() {
		ResourceList resources = player.getResources();
		if(resources.count() > 7){
			int toDiscard = resources.count()/2;
			Set<Integer> discardingLocations = new HashSet<Integer>();
			while(discardingLocations.size() < toDiscard){
				int nextCard = random.nextInt(resources.count());
				if(!discardingLocations.contains(nextCard)){
					discardingLocations.add(nextCard);
				}
			}
			Map<ResourceType, Integer> discardMap = getTradeList();
			
			int x = 0;
			for(ResourceType resource : ResourceType.values()){
				for(int i = 0; i < resources.count(resource); ++i){
					if(discardingLocations.contains(x + i)){
						int newInt = discardMap.get(resource);
						++newInt;
						discardMap.remove(resource);
						discardMap.put(resource, newInt);
					}
				}
				x = x + resources.count(resource);
			}
			
			ResourceList discarding = new ResourceList(discardMap);
			try{
				server.discardCards(player.getUUID(), game.getUUID(), discarding);
			}
			catch(Exception e){
				System.out.println("Server Error: Could not discard cards");
			}
		}
	}

	@Override
	public void robber() {
		while(true){
			int x = random.nextInt(5) - 2;
			int y = random.nextInt(5) - 2;
			HexLocation hex = new HexLocation(x,y);
			if(game.getCatanModel().getMap().canMoveRobberTo(hex)){
				Collection<Municipality> settlements = game.getCatanModel().getMap().getMunicipalitiesAround(hex);
				List<UUID> uuids = new ArrayList<UUID>();
				for(Municipality m : settlements){
					if(!uuids.contains(m.getOwner().getPlayerUUID())){
						uuids.add(m.getOwner().getPlayerUUID());
					}
				}
				if(uuids.contains(player.getUUID())){
					continue;
				}
				try{
					if(uuids.size() == 0){
						if(soldier){
							server.soldier(player.getUUID(), game.getUUID(), hex, null);
							soldier = false;
						}
						else{
							server.robPlayer(player.getUUID(), game.getUUID(), hex, null);
						}
						return;
					}
					int playerToRob = random.nextInt(uuids.size());
					UUID robbedPlayerUUID = uuids.get(playerToRob);
					if(soldier){
						server.soldier(player.getUUID(), game.getUUID(), hex, robbedPlayerUUID);
						soldier = false;
					}
					else{
						server.robPlayer(player.getUUID(), game.getUUID(), hex, robbedPlayerUUID);
					}
					return;
				}
				catch(Exception e){
					System.out.println("Server Error: Could not rob player");
				}
			}
		}
	}

	private void yearOfPlenty(){
		int first = random.nextInt(5);
		int second = random.nextInt(5);
		ResourceType type1 = ResourceType.values()[first];
		ResourceType type2 = ResourceType.values()[second];
		try{
			server.yearOfPlenty(player.getUUID(), game.getUUID(), type1, type2);
		}
		catch(Exception e){
			System.out.println("Server Exception: Could not use Year of Plenty");
		}
	}
	private void monopoly(){
		int number = random.nextInt(5);
		ResourceType type = ResourceType.values()[number];
		try{
			server.monopoly(player.getUUID(), game.getUUID(), type);
		}
		catch(Exception e){
			System.out.println("Server Exception: Could not use Monopoly");
		}	
	}
	private void roadBuilding(){
		ModelFacade model = game;
		try{
			int counter = 5;
			while(true){
				--counter;
				if(counter < 0){
					return;
				}
				game = model;
				EdgeLocation road1 = buildRoad();
				if(road1 == null){
					continue;
				}
				game.buildRoad(player.getReference(), road1);
				EdgeLocation road2 = buildRoad();
				if(road2 == null){
					continue;
				}
				server.roadBuilding(player.getUUID(), game.getUUID(), road1, road2);
				return;
			}
		}
		catch(Exception e){
			System.out.println("Server Error: Could not use RoadBuilding");
		}
	}
	private EdgeLocation buildRoad(){
		int counter = 500;
		while(true){
			--counter;
			if(counter < 0){
				return null;
			}
			int x = random.nextInt(5) - 2;
			int y = random.nextInt(5) - 2;
			int direction = random.nextInt(6);
			EdgeDirection dir = selectEdge(direction);
			EdgeLocation edge = new EdgeLocation(x, y, dir);
			if(game.canBuildRoad(edge)){
				return edge;
			}
		}
	}
	private void buildSettlement(){
		//150 possible choices 5 rows x 5 columns x 6 vertexes
		//to leave room for duplicates, we will cap it at 500
		int counter = 500;
		while(true){
			--counter;
			if(counter < 0){
				return;
			}
			int x = random.nextInt(5) - 2;
			int y = random.nextInt(5) - 2;
			int direction = random.nextInt(6);
			VertexDirection dir = selectVertex(direction);
			VertexLocation vertex = new VertexLocation(x, y, dir);
			if(game.canBuildSettlement(vertex)){
				try{
					server.buildSettlement(player.getUUID(), game.getUUID(), vertex);
					return;
				}
				catch(Exception e){
					System.out.println("Server Error: Could not build settlement");
				}
			}
		}
	}
	private void buildCity(){
		Collection<Municipality> municipalities = game.getCatanModel().getMap().getMunicipalities();
		List<Municipality> settlements = new ArrayList<Municipality>();
		for(Municipality m : municipalities){
			if(m.getType().equals(MunicipalityType.SETTLEMENT)){
				settlements.add(m);
			}
		}
		
		int number = random.nextInt(settlements.size());
		try{
			server.buildCity(player.getUUID(), game.getUUID(), settlements.get(number).getLocation());
		}
		catch(Exception e){
			System.out.println("Server Error: Could not build city");
		}
	}
	private void offerTrade(){
		int tradeRecipient = random.nextInt(3);
		List<Player> players = game.getCatanModel().getPlayers();
		for(int i = 0; i < players.size(); ++i){
			if(players.get(i).getUUID().equals(player.getUUID())){
				players.remove(i);
				break;
			}
		}
		
		Player recipient = players.get(tradeRecipient);
		
		ResourceList myResources = player.getResources();
		ResourceList theirResources = recipient.getResources();
		
		Map<ResourceType, Integer> resources = getTradeList();
		
		ResourceTradeList tradeList = new ResourceTradeList(resources, resources);
		
		TradeOffer offer = new TradeOffer(player.getReference(), recipient.getReference(), tradeList);
		offer = generateTrade(theirResources, offer, null);
		ResourceType chosen = ResourceType.BRICK;
		for(ResourceType type : ResourceType.values()){
			if(offer.getOffer().getWanted().get(type) != 0){
				chosen = type;
				break;
			}
		}
		offer = generateTrade(myResources, offer, chosen);
		
		try{
			server.offerTrade(player.getUUID(), game.getUUID(), offer.getOffer(), recipient.getUUID());
		}
		catch(Exception e){
			System.out.println("Server Error: Could not offer trade");
		}
	}
	private void maritimeTrade(ResourceType type){
		List<ResourceType> types = new ArrayList<ResourceType>();
		for(ResourceType r : ResourceType.values()){
			if(!r.equals(type)){
				types.add(r);
			}
		}
		int number = random.nextInt(types.size());
		int ratio = getRatio(type);
		try{
			server.maritimeTrade(player.getUUID(), game.getUUID(), type, types.get(number), ratio);
		}
		catch(Exception e){
			System.out.println("Server Error: Could not maritimeTrade");
		}
	}	

	private int getRatio(ResourceType type){
		int ratio = 4;
		Collection<Port> ports = game.getCatanModel().getMap().getPorts();
		for(Port p : ports){
			PlayerReference owner = game.getCatanModel().getMap().getPortOwner(p);
			if(owner == null){
				continue;
			}
			if(owner.getPlayerUUID().equals(player.getUUID())){
				ResourceType resourceType = p.getResource();
				if(p.getRatio() == 3){
					ratio = 3;
					continue;
				}
				if(resourceType.equals(type)){
					ratio = 2;
					break;
				}
			}
		}
		return ratio;
	}
	private TradeOffer generateTrade(ResourceList resources, TradeOffer trade, ResourceType requested){
		Map<ResourceType, Integer> tradeList = new HashMap<ResourceType, Integer>();
		
		ResourceType[] resourceArray = ResourceType.values();
		List<ResourceType> resourceList = new ArrayList<ResourceType>();
		for(ResourceType type : resourceArray){
			if(resources.count(type) != 0){
				if(requested == null || !type.equals(requested)){
					resourceList.add(type);
				}
			}
		}
		
		int randomPick = random.nextInt(resourceList.size());
		ResourceType pick = resourceList.get(randomPick);
		int countOfType = resources.count(pick);
		int countPick = random.nextInt(countOfType);
		
		for(ResourceType type : resourceArray){
			if(type.equals(pick)){
				tradeList.put(type, countPick + 1);
			}
			else{
				tradeList.put(type, 0);
			}
		}
		
		ResourceTradeList offer;
		if(requested != null){
			offer = new ResourceTradeList(tradeList, trade.getOffer().getWanted());
		}
		else{
			offer = new ResourceTradeList(trade.getOffer().getOffered(), tradeList);
		}
		return new TradeOffer(trade.getSender(), trade.getReceiver(), offer);
	}
	private Map<ResourceType, Integer> getTradeList(){
		Map<ResourceType, Integer> discardMap = new HashMap<ResourceType, Integer>();
		discardMap.put(ResourceType.BRICK, 0);
		discardMap.put(ResourceType.ORE, 0);
		discardMap.put(ResourceType.SHEEP, 0);
		discardMap.put(ResourceType.WHEAT, 0);
		discardMap.put(ResourceType.WOOD, 0);
		return discardMap;
	}
	private VertexDirection selectVertex(int location){
		switch (location){
		case 0:
			return VertexDirection.East;
		case 1:
			return VertexDirection.NorthEast;
		case 2:
			return VertexDirection.NorthWest;
		case 3:
			return VertexDirection.SouthEast;
		case 4:
			return VertexDirection.SouthWest;
		case 5:
			return VertexDirection.West;
		}
		return null;
	}
	private EdgeDirection selectEdge(int location){
		switch (location){
		case 0:
			return EdgeDirection.North;
		case 1:
			return EdgeDirection.NorthEast;
		case 2:
			return EdgeDirection.NorthWest;
		case 3:
			return EdgeDirection.SouthEast;
		case 4:
			return EdgeDirection.SouthWest;
		case 5:
			return EdgeDirection.South;
		}
		return null;
	}
}
