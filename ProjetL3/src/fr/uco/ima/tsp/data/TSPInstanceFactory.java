package fr.uco.ima.tsp.data;

import fr.uco.ima.tsp.io.TSPLIBInstanceReader;

/**
 * Class responsible to build the instances
 * 
 * @author froger
 *
 */
public class TSPInstanceFactory {

	public enum InstanceName {
		wi29, eil51, berlin52, st70, eil76, kroA100, kroC100, eil101, ch130, ch150, pr226, pr439, uy734, lu980, ca4663, it16862;

		public static InstanceName getName(int instanceChoix) {
			switch (instanceChoix) {
			case 1:
				return wi29;
			case 2:
				return eil51;
			case 3:
				return berlin52;
			case 4:
				return st70;
			case 5:
				return eil76;
			case 6:
				return kroA100;
			case 7:
				return kroC100;
			case 8:
				return eil101;
			case 9:
				return ch130;
			case 10:
				return ch150;
			case 11:
				return pr226;
			case 12:
				return pr439;
			case 13:
				return uy734;
			case 14:
				return lu980;
			case 15:
				return ca4663;
			case 16:
				return it16862;
			default:
				return null;
			}
		}
	}

	public static TSPInstance getInstance(InstanceName name) {
		TSPLIBInstanceReader builder = new TSPLIBInstanceReader();
		TSPInstance instance = builder.readInstance("./instances/" + name.toString() + ".tsp");
		return instance;
	}

}
