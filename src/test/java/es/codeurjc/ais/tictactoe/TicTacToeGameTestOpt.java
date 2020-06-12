package es.codeurjc.ais.tictactoe;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerResult;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerValue;

public class TicTacToeGameTestOpt {

	//variables
	private TicTacToeGame tictactoegame = new TicTacToeGame();
	
	private Connection conexion1 = mock(Connection.class);
	private Connection conexion2 = mock(Connection.class);
	
	private Player jugador1 = new Player(1, "x", "jugador1");
	private Player jugador2 = new Player(2, "o", "jugador2");
	
	private List<Player> arrayJugadores = new CopyOnWriteArrayList<>();
	
	public void resetearConexiones(Connection c1, Connection c2) {
		reset(c1);
		reset(c2);
	}
	
	public void establecerconexion(TicTacToeGame game, Connection c1, Connection c2, Player j1, Player j2, List<Player> arrayJugadores) {
		
		//Comprobamos las conexiones
		tictactoegame.addConnection(conexion1);
		tictactoegame.addConnection(conexion2);
		arrayJugadores.add(j1);
		
		game.addPlayer(j1);
		verify(c1).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
		verify(c2).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
		
		arrayJugadores.add(j2);
		game.addPlayer(j2);
		verify(c1, times(2)).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
		verify(c2, times(2)).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
		
		
		//Comprobamos los turnos
		//Ambas conexiones para el jugador1
		verify(c1).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
		verify(c2).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
		
		resetearConexiones(c1, c2);
		
	}
	
	public void verificarCambioturno(Connection conexion1, Connection conexion2, List<Player> arrayJugadores, int times) {
		//verifico que se reciben los cambios de turno

		verify(conexion1, times(times)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
		verify(conexion1, times(times)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(1)));
		
		verify(conexion2, times(times)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
		verify(conexion2, times(times)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(1)));
	}
	
	@DisplayName("Se comprueba que gana el Jugador 1")
	@Test
	public void testGanaJugador1() {
	
		establecerconexion(tictactoegame, conexion1, conexion2, jugador1, jugador2, arrayJugadores);
		
		//hago una jugada
		tictactoegame.mark(0); //j1
		tictactoegame.mark(2); //j2
		tictactoegame.mark(3); //j1
		tictactoegame.mark(8); //j2
		
		verificarCambioturno(conexion1, conexion2, arrayJugadores, 2);
		
		//hago ganar al jugador 1
		tictactoegame.mark(6);
		
		ArgumentCaptor<WinnerValue> winval = ArgumentCaptor.forClass(WinnerValue.class);
		verify(conexion1).sendEvent(eq(EventType.GAME_OVER), winval.capture());
		
		WinnerValue valor = (WinnerValue) winval.getValue();
		//compruebo que la conexion2 recibe el valor
		verify(conexion2).sendEvent(eq(EventType.GAME_OVER),eq(valor));
		
		assertEquals(valor.player, jugador1); // ha ganado el jugador 1
		assertNotEquals(valor.player, jugador2); //ha perdido el jugador 2
		
		resetearConexiones(conexion1, conexion2);
		
	}
	
	@DisplayName("Se comprueba que pierde el Jugador 1 y gana el jugador 2")
	@Test
	public void testGanaJugador2() {

		establecerconexion(tictactoegame, conexion1, conexion2, jugador1, jugador2, arrayJugadores);
	
		//hago una jugada
		tictactoegame.mark(0); //j1
		tictactoegame.mark(2); //j2
		tictactoegame.mark(3); //j1
		tictactoegame.mark(5); //j2
		
		verificarCambioturno(conexion1, conexion2, arrayJugadores, 2);

		//marca el jugador1
		tictactoegame.mark(7);
		
		//hago ganar al jugador2
		tictactoegame.mark(8);
		
		ArgumentCaptor<WinnerValue> winval = ArgumentCaptor.forClass(WinnerValue.class);
		verify(conexion1).sendEvent(eq(EventType.GAME_OVER), winval.capture());
		
		WinnerValue valor = (WinnerValue) winval.getValue();
		//compruebo que la conexion2 recibe el valor
		verify(conexion2).sendEvent(eq(EventType.GAME_OVER),eq(valor));
		
		assertEquals(valor.player, jugador2); // ha ganado el jugador 2
		assertNotEquals(valor.player, jugador1); //ha perdido el jugador 1
		
		resetearConexiones(conexion1, conexion2);
	}
	
	@DisplayName("Se comprueba que empatan ambos jugadores")
	@Test
	public void testEmpatan() {
		
		establecerconexion(tictactoegame, conexion1, conexion2, jugador1, jugador2, arrayJugadores);
				
		//hago una jugada
		tictactoegame.mark(0); //j1
		tictactoegame.mark(2); //j2
		tictactoegame.mark(1); //j1
		tictactoegame.mark(3); //j2
		tictactoegame.mark(5); //j1
		tictactoegame.mark(4); //j2
		tictactoegame.mark(6); //j1
		tictactoegame.mark(7); //j2
		
		verificarCambioturno(conexion1, conexion2, arrayJugadores, 4);

		//marca el jugador1
		tictactoegame.mark(8);
	
		ArgumentCaptor<WinnerResult> winRes = ArgumentCaptor.forClass(WinnerResult.class);
		verify(conexion1).sendEvent(eq(EventType.GAME_OVER), winRes.capture());
		
		WinnerResult valor = (WinnerResult) winRes.getValue();
		boolean haEmpatado = tictactoegame.checkDraw();
		//compruebo que la conexion2 recibe el valor
		verify(conexion2).sendEvent(eq(EventType.GAME_OVER),eq(valor));
		
		assertNull(valor); //cuando empatan el valor del captor es null
		assertEquals(haEmpatado, true); //compruebo si han empatado
		
		resetearConexiones(conexion1, conexion2);
	}		
}