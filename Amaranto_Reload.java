package mx.itesm.prueba1;

import java.util.Random;

import mx.itesm.prueba1.DD_03.Estado;
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
		HUIR,
		CHOCAR,
		CHOCAR_PARED,
	    DER,
		UNO_UNO
	}
	
	private int poder=1;
	private Estados estado;
	
	private long timer=System.currentTimeMillis();
	
	private double canion;//cañon
	private double radar;//radar
	private double tanque;//cuerpo del cañon
	
	private boolean uno_uno=false;
	private Random random= new Random();
	private HitRobotEvent hre;
	private HitWallEvent hwe;
	private double a;

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
				a++;
				execute();
				break;
			case HUIR:
				doNothing();
				break;
			case CHOCAR:
				chocar();
				break;
			case CHOCAR_PARED:
				chocarPared();
				break;
			case DER:
				setTurnRadarRightRadians(Utils.normalRelativeAngle(radar)); //centra al enemigo
			    setTurnGunRightRadians(Utils.normalRelativeAngle(canion)); //centra al enemigo
				setFire(poder);
				
				setTurnRightRadians(50*Math.sin(a/10));
				setAhead(10);
				a++;
				execute();
				break;
			case UNO_UNO:
				setTurnRadarRightRadians(Utils.normalRelativeAngle(radar)); // centra al enemigo
			    setTurnGunRightRadians(Utils.normalRelativeAngle(canion));  // centra al enemigo
			    setFire(poder);
			    setTurnRightRadians(50*Math.sin(a/10));
				setAhead(10);
				a++;
			    execute();
				break;
			default:
				doNothing();
				break;
			}
		}
		
	}
	
	private void chocarPared() {
		setBack(20);
		setTurnRight(180);
		execute();
		estado = Estados.AVANZAR;
	}

	private void chocar() {
		setBack(20);
		setTurnRight(90);
		execute();
		estado=Estados.AVANZAR;
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		this.radar = event.getBearingRadians() + getHeadingRadians() - getRadarHeadingRadians();
		this.canion = event.getBearingRadians() + getHeadingRadians() - getGunHeadingRadians();
		this.tanque = event.getBearingRadians() + getHeadingRadians() - getHeadingRadians();
		
		if(uno_uno) {
			estado=Estados.UNO_UNO;
		}
		else {
			estado=Estados.DER;
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
		hwe=event;
	}
	

	private void inicializar() {
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true); 
		estado=Estados.AVANZAR;
		a=0;
	}
}
