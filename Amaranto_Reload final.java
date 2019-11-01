package paco;

import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Amaranto_Reload extends AdvancedRobot{
	public enum Estados{
		AVANZAR,
		CHOCAR,
		CHOCAR_PARED,
	    DISPARO_SCAN,
		UNO_UNO
	}
	
	//Variable de estado
	private Estados estado;
	
	//Variables de Movimiento
	private int poder=1;
	private double canion;//canon
	private double radar;//radar
	private double tanque;//cuerpo
	private double a;
	
	//Banderas
	private boolean choquePared=false;
	private boolean choqueRobot=false;
	private boolean uno_uno=false;

	private HitRobotEvent hre;


	@Override
	public void run() {
		inicializar();
		while(true) {
			switch (estado) {
			case AVANZAR:
				setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
				setTurnGunRightRadians(Double.POSITIVE_INFINITY);
				setTurnRightRadians(50*Math.sin(a/10));
				setAhead(10);
				if(a>=20*Math.PI) {
					a=0;
				}else {
					a++;
				}
				execute();
				break;
			case CHOCAR:
				chocar();
				break;
			case CHOCAR_PARED:
				chocarPared();
				break;
			case DISPARO_SCAN:
				setTurnRadarRightRadians(Utils.normalRelativeAngle(radar)); //centra al enemigo
			    setTurnGunRightRadians(Utils.normalRelativeAngle(canion)); //centra al enemigo
				setFire(poder);
				setTurnRightRadians(50*Math.sin(a/10));
				setAhead(10);
				if(a>=20*Math.PI) {
					a=0;
				}else {
					a++;
				}
				execute();
				break;
			case UNO_UNO:
				setTurnRadarRightRadians(Utils.normalRelativeAngle(radar)); // centra al enemigo en el radar
			    setTurnGunRightRadians(Utils.normalRelativeAngle(canion));  // centra al enemigo en el cañon
			    setTurnRightRadians(Utils.normalRelativeAngle(tanque));// centra el tanque contra el enemigo
			    setAhead(50);
			    setFire(poder);
			    execute();
				break;
			default:
				doNothing();
				break;
			}
		}
		
	}
	
	private void chocarPared() {
		stop();
		back(20);
		turnRight(180);
		if((getX()>=700&&getY()>=500)||(getX()<=100&&getY()<=100)||(getX()>=700&&getY()<=100)||(getX()<=100&&getY()>=500)) {
			ahead(25);
		}
		choquePared=false;
		estado = Estados.AVANZAR;
	}

	private void chocar() {
		stop();
		if(!uno_uno) {
			if(hre.getBearing()>getHeading()-5&&hre.getBearing()<getHeading()+5) {
				ahead(20);
			}else {
				back(20);
			}
			if(!hre.isMyFault()) {
				turnRight(180);
			}
		}
		choqueRobot=false;
		estado=Estados.AVANZAR;
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		radar = event.getBearingRadians() + getHeadingRadians() - getRadarHeadingRadians();
		canion = event.getBearingRadians() + getHeadingRadians() - getGunHeadingRadians();
		tanque = event.getBearingRadians();
		
		if(uno_uno) {
			estado=Estados.UNO_UNO;
		}
		else if(!choquePared && !choqueRobot){ 
			estado=Estados.DISPARO_SCAN;
		}
		
	}
	
	@Override
	public void onBulletHit(BulletHitEvent event) {
		poder=3;
	}
	
	@Override
	public void onBulletMissed(BulletMissedEvent event) {
		poder=1;
	}
	
	@Override
	public void onHitRobot(HitRobotEvent event) {
		hre = event;
		choqueRobot=true;
		estado = Estados.CHOCAR;
	}
	
	@Override
	public void onRobotDeath(RobotDeathEvent event) {
		if(getOthers()==1) {
			uno_uno=true;
			estado=Estados.UNO_UNO;
		}
	}
	
	@Override
	public void onHitWall(HitWallEvent event) {
		estado = Estados.CHOCAR_PARED;
		choquePared=true;
	}
	

	private void inicializar() {
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true); 
		estado=Estados.AVANZAR;
		a=0;
	}
}
