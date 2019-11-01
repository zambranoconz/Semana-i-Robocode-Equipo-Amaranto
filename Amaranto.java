package paco;

import java.util.Arrays;
import java.util.Dictionary;
//import java.util.HashMap;
// import java.util.Map;

import robocode.AdvancedRobot;
import robocode.Bullet;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class Amaranto extends AdvancedRobot {
	private enum Estados{
		CAMBIAR_RUTA,
		ACERCAR_DISPARAR,
		HUIR,
		ATACAR,
		ANALIZAR,
		DISPARAR,
		ACTUALIZAR,
		APUNTAR
	}
	private enum Estrategias{
		SIETE_CINCO,
		CUATRO_DOS,
		UNO_UNO_DEF,
		UNO_UNO_R
	}
	//estados de combate
	private Estados estado;
	private Estados guardarEstado;
	
	//estados de la estrategia a usar
	private Estrategias estrategia;
	
	//variables necesarias para usar dentro de los algoritmos
	private Bullet otherbullet;
	private Bullet[] myBullets;
	
	//Dimensiones
	private double width;
	private double height;
	
	
	//Mov
	private int x;
	private int y;
	private int a=0;
	private int battlefieldWidth;
	private int battlefieldHeight;
	private int diffX;
	private int diffY;
	private double bound;
	
	
	private int others;
	private Dictionary<String,double[]> othersMap;
	
	
	//eventos necesarios para tomar decisiones 
	private BulletHitEvent bhe;
	private BulletHitBulletEvent bhbe;
	private HitByBulletEvent hbbe;
	private HitRobotEvent hre;
	private HitWallEvent hwe;
	private RobotDeathEvent rde;
	private ScannedRobotEvent sre;
	
	@Override
	public void run() {
		inicializar();
		while(true) {
			try {
			switch (estrategia) {
			case SIETE_CINCO:
				switch (estado) {
				case CAMBIAR_RUTA:
					buscarExtremos();
					break;
				case ACERCAR_DISPARAR:
					doNothing();
					break;
				case HUIR:
					doNothing();
					break;
				case ATACAR:
					doNothing();
					break;
				case ANALIZAR:
					analizar();
					break;
				case DISPARAR:
					doNothing();
					break;
				case ACTUALIZAR:
					actualizar();
					break;
				default:
					doNothing();
					break;
				}
				break;
			case CUATRO_DOS:
				switch (estado) {
				case CAMBIAR_RUTA:
					doNothing();
					break;
				case ACERCAR_DISPARAR:
					doNothing();
					break;
				case HUIR:
					doNothing();
					break;
				case ATACAR:
					doNothing();
					break;
				case ANALIZAR:
					analizar();
					break;
				case DISPARAR:
					doNothing();
					break;
				case ACTUALIZAR:
					doNothing();
					break;
				default:
					doNothing();
					break;
				}
				break;
			case UNO_UNO_DEF:
				switch (estado) {
				case CAMBIAR_RUTA:
					doNothing();
					break;
				case ACERCAR_DISPARAR:
					doNothing();
					break;
				case HUIR:
					doNothing();
					break;
				case ATACAR:
					doNothing();
					break;
				case ANALIZAR:
					analizar();
					break;
				case DISPARAR:
					doNothing();
					break;
				case ACTUALIZAR:
					doNothing();
					break;
				default:
					doNothing();
					break;
				}
				break;
			case UNO_UNO_R:
				switch (estado) {
				case CAMBIAR_RUTA:
					doNothing();
					break;
				case ACERCAR_DISPARAR:
					doNothing();
					break;
				case HUIR:
					doNothing();
					break;
				case ATACAR:
					doNothing();
					break;
				case ANALIZAR:
					analizar();
					break;
				case DISPARAR:
					doNothing();
					break;
				case ACTUALIZAR:
					doNothing();
					break;
				default:
					doNothing();
					break;
				}
				break;
			default:
				doNothing();
				break;
			}
			}catch(NullPointerException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void actualizar() {
		othersMap.remove(rde.getName());
		estrategia = elegirEstrategia();
		estado = guardarEstado;
	}

	private void inicializar() {
		width = getWidth();
		height = getHeight();
		
		estado=Estados.CAMBIAR_RUTA;
		estrategia=elegirEstrategia();
		
		setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);

		x = (int) Math.round(getX());
		y = (int) Math.round(getY());
		battlefieldWidth = (int) getBattleFieldWidth();
		battlefieldHeight = (int) getBattleFieldHeight();
		
		
	}

	private Estrategias elegirEstrategia() {
		if(getOthers()>4) {
			return Estrategias.SIETE_CINCO;
		}
		else if(getOthers()>1) {
			return Estrategias.CUATRO_DOS;
		}
		else {
			try {
				if(getEnergy()-36>sre.getEnergy()){
					return Estrategias.UNO_UNO_R;
				}
			}
			catch(NullPointerException e) {/*no se pase la excepcion*/}
			
			return Estrategias.UNO_UNO_DEF;
		}
	}

	// analizar()
	// Estrategias: Todas
	
	/*
	 * 
	 * Tenemos declarado un Map<String,double[]> para guardar valores actualizados de los robots pues nuestro radar estará girando indefinidamente a la derecha
	 * La key de tipo String está basada en que los nombres de los robots no se repiten
	 * El value de tipo double[] contiene datos pertinentes de cada enemigo
	 * double[0] = distancia
	 * double[1] = direccion
	 * double[2] = velocidad
	 * double[3] = energia (Vida)
	 * 
	 */
	
	private void analizar() {
		
		double[] dataScannedEnemy = new double[4];
		dataScannedEnemy[0] = sre.getDistance();
		dataScannedEnemy[1] = sre.getHeading();
		dataScannedEnemy[2] = sre.getVelocity();
		dataScannedEnemy[3] = sre.getEnergy();
		
		if(othersMap.get(sre.getName())==null) {
			othersMap.put(sre.getName(), dataScannedEnemy);
		}else {
			for(int i=0;i<4;i++) {
				if(dataScannedEnemy[i]!=othersMap.get(sre.getName())[i]) {
					othersMap.get(sre.getName())[i] = dataScannedEnemy[i];
				}
			}
		}
		
		estado = guardarEstado;
	}

	// buscarExtremos()
	// Estrategias: 7-5
	
	/* 	
	 * 
	 * Nuestra estrategia es alejarnos del centro, esto cuando hay de 7 a 5 enemigos
	 * El método compara nuestra posicion con los extremos (las paredes) del campo de batalla
	 * permitiendonos saber el y encaminarnos al mas cercano.
	 * 
	 */
	
	private void buscarExtremos() {
		x = (int) Math.round(getX());
		y = (int) Math.round(getY());
		diffX = battlefieldWidth-x;
		diffY = battlefieldHeight-y;
		
		int minX = Math.min(x, diffX);
		int minY = Math.min(y, diffY);
		
		int minTot = Math.min(minX, minY);
		
		double heading = getHeading();
		
		if(minTot==x) {
			if(heading<=270||heading>=90) {
				turnRight(270-heading);
			}else {
				if(heading<90) {
					turnLeft(heading+90);
				}else { 					//if (heading>270)
					turnLeft(450-heading);	//450 = 360 + 90
				}
			}
			ahead(400);
			bound = height+15;
		}else if(minTot==y) {
			if(heading<=180) {
				turnRight(180-heading);
			}else {
				turnLeft(heading-180);
			}
			ahead(400);
			bound = height+15;
		}else if(minTot==diffX) {
			if(heading<=270||heading>=90) {
				turnLeft(90-heading);
			}else {
				if(heading<90) {
					turnRight(90-heading);
				}else { 					//if (heading>270)
					turnLeft(450-heading);	//450 = 360 + 90
				}
			}
			ahead(400);
			bound = battlefieldWidth-(height+15);
		}else{ //if(minTot==diffY)
			if(heading<=180) {
				turnLeft(heading);
			}else {
				turnRight(360-heading);
			}
			ahead(400);
			bound = battlefieldHeight-(height+15);
		}
		
	}
	
	@Override
	public void onRobotDeath(RobotDeathEvent event) {
		rde = event;
		guardarEstado = estado;
		estado = Estados.ACTUALIZAR;
	}
	
	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		sre = event;
		guardarEstado = estado;
		estado = Estados.ANALIZAR;
	}
}
