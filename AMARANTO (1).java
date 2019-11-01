package mx.itesm.prueba1;

import java.util.Scanner;

import robocode.AdvancedRobot;
import robocode.BattleRules;
import robocode.Bullet;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.control.events.BattleStartedEvent;

public class AMARANTO extends AdvancedRobot {
	private enum Estados{
		CAMBIAR_RUTA,
		ACERCAR_DISPARAR,
		HUIR,
		ATACAR,
		ANALIZAR,
		DISPARAR,
		ACTUALIZAR
	}
	private enum Estrategias{
		SIETE_CINCO,
		CUATRO_DOS,
		UNO_UNO_DEF,
		UNO_UNO_R
	}
	//estados de combate
	private Estados estado;
	
	//estados de la estrategia a usar
	private Estrategias estrategia;
	
	//variables necesarias para usar dentro de los algoritmos
	private Bullet otherbullet;
	private Bullet[] myBullets;
	
	//eventos necesarios para tomar decisiones 
	private BulletHitEvent bhe;
	private BulletHitBulletEvent bhbe;
	private HitByBulletEvent hbbe;
	private HitRobotEvent hre;
	private HitWallEvent hwe;
	private RobotDeathEvent rde;
	private ScannedRobotEvent scan;
	//largo y acho de la pared
	private int width;
	private int height;
	//coordenadas del robot
	private int X;
	private int Y;
	private int a=0;
	
	@Override
	public void run() {
		inicializar();
		while(true) {
			switch (estrategia) {
			case SIETE_CINCO:
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
					//buscarExtremos();
					turnRadarLeft(10);
					execute();
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
		}
	}

	private void buscarExtremos() {
		//coordenadas
		Y=(int)Math.round(this.getY());
		X=(int)Math.round(this.getX());
		//que pared esta mas cercana
		int difAParedDerecha=width-X;
		int difAParedArriba=height-Y;
		
		int minDif=(int) Math.min(difAParedDerecha, difAParedArriba);
		int minXY=(int) Math.min(X, Y);
		int minTot=(int)Math.min(minDif, minXY);
		setTurnGunRight(scan.);
		execute();
		
		//if(minTot==difAParedDerecha) {
			//while (true/*aqui va si sigue vivo el enemigo que estabamos siguiendo*/) {
				//setAhead(10);
				//setTurnRight(Math.toDegrees(50*Math.sin(a/10)));
				//execute();
				//a++;
			//}
			/*aqui se hace el cambio de estado */
		
			
			
	//	}
		//else if(minTot==difAParedArriba) {
			
		//}
		//else if(minTot==X) {
			
	//	}
		//else /*cuando el minimo es Y */{
			
		//}
		
	}

	private void inicializar() {
		estado=Estados.CAMBIAR_RUTA;
		estrategia=elegirEstrategia();
		//independiza el movimiento de las partes cañon,radar y las ruedas por asi decirlo
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		//largo y ancho del estadio
		 width=(int)this.getBattleFieldWidth();
		 height=(int)this.getBattleFieldHeight();
		//coordenadas del robot
		Y=(int)Math.round(this.getY());
		X=(int)Math.round(this.getX());
		//colores de cada una de las partes del robot
		
		
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
				if(getEnergy()-36>scan.getEnergy()){
					return Estrategias.UNO_UNO_R;
				}
			}
			catch(NullPointerException e) {
				//no se pase la excepcion
			}
			return Estrategias.UNO_UNO_DEF;
		}
	}
	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		scan=event;
	}
}
