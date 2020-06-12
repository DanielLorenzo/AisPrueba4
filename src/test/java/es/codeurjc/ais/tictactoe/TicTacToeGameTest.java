package es.codeurjc.ais.tictactoe;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerResult;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerValue;

public class TicTacToeGameTest {
	@DisplayName("Test de conexiones")
	
	@Test
	public void testConexion() {
		
		//Comprobamos las conexiones
		
		TicTacToeGame tictactoegame = new TicTacToeGame();
		
		Connection conexion1 = mock(Connection.class);
		Connection conexion2 = mock(Connection.class);
		
		tictactoegame.addConnection(conexion1);
		tictactoegame.addConnection(conexion2);
		
		Player jugador1 = new Player(1, "x", "jugador1");
		Player jugador2 = new Player(2, "o", "jugador2");
		
		List<Player> arrayJugadores = new CopyOnWriteArrayList<>();
		arrayJugadores.add(jugador1);
		
		tictactoegame.addPlayer(jugador1);
		verify(conexion1).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
		verify(conexion2).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
		
		arrayJugadores.add(jugador2);
		tictactoegame.addPlayer(jugador2);
		verify(conexion1, times(2)).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
		verify(conexion2, times(2)).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
		
		
		//Comprobamos los turnos
		//Ambas conexiones para el jugador1
		verify(conexion1).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
		verify(conexion2).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
		
		reset(conexion1);
		reset(conexion2);
		
		
		//hago una jugada
		tictactoegame.mark(0); //j1
		tictactoegame.mark(2); //j2
		tictactoegame.mark(3); //j1
		tictactoegame.mark(8); //j2
		
		//verifico que se reciben los cambios de turno
		
		verify(conexion1, times(2)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
		verify(conexion1, times(2)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(1)));
		
		verify(conexion2, times(2)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
		verify(conexion2, times(2)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(1)));
		

		//hago ganar al jugador 1
		tictactoegame.mark(6);
		
		ArgumentCaptor<WinnerValue> winval = ArgumentCaptor.forClass(WinnerValue.class);
		verify(conexion1).sendEvent(eq(EventType.GAME_OVER), winval.capture());
		
		WinnerValue valor = (WinnerValue) winval.getValue();
		//compruebo que la conexion2 recibe el valor
		verify(conexion2).sendEvent(eq(EventType.GAME_OVER),eq(valor));
		
		assertNotNull(valor);// si fuese null entonces habrian empatado
		assertEquals(valor.player, jugador1); // ha ganado el jugador 1
		assertNotEquals(valor.player, jugador2); //ha perdido el jugador 2
		
		
		reset(conexion1);
		reset(conexion2);
		
	}
	
	@Test
	public void testGanaJugador2() {
		//Comprobamos las conexiones
		
				TicTacToeGame tictactoegame = new TicTacToeGame();
				
				Connection conexion1 = mock(Connection.class);
				Connection conexion2 = mock(Connection.class);
				
				tictactoegame.addConnection(conexion1);
				tictactoegame.addConnection(conexion2);
				
				Player jugador1 = new Player(1, "x", "jugador1");
				Player jugador2 = new Player(2, "o", "jugador2");
				
				List<Player> arrayJugadores = new CopyOnWriteArrayList<>();
				arrayJugadores.add(jugador1);
				
				tictactoegame.addPlayer(jugador1);
				verify(conexion1).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
				verify(conexion2).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
				
				arrayJugadores.add(jugador2);
				tictactoegame.addPlayer(jugador2);
				verify(conexion1, times(2)).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
				verify(conexion2, times(2)).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
				
				
				//Comprobamos los turnos
				//Ambas conexiones para el jugador1
				verify(conexion1).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
				verify(conexion2).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
				
				reset(conexion1);
				reset(conexion2);
				
				
				//hago una jugada
				tictactoegame.mark(0); //j1
				tictactoegame.mark(2); //j2
				tictactoegame.mark(3); //j1
				tictactoegame.mark(5); //j2
				
				//verifico que se reciben los cambios de turno
				
				verify(conexion1, times(2)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
				verify(conexion1, times(2)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(1)));
				
				verify(conexion2, times(2)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
				verify(conexion2, times(2)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(1)));
				

				//marca el jugador1
				tictactoegame.mark(7);
				
				//hago ganar al jugador2
				tictactoegame.mark(8);
				
				ArgumentCaptor<WinnerValue> winval = ArgumentCaptor.forClass(WinnerValue.class);
				verify(conexion1).sendEvent(eq(EventType.GAME_OVER), winval.capture());
				
				WinnerValue valor = (WinnerValue) winval.getValue();
				//compruebo que la conexion2 recibe el valor
				verify(conexion2).sendEvent(eq(EventType.GAME_OVER),eq(valor));
				
				assertNotNull(valor);// si fuese null entonces habrian empatado
				assertEquals(valor.player, jugador2); // ha ganado el jugador 2
				assertNotEquals(valor.player, jugador1); //ha perdido el jugador 1
				
				
				reset(conexion1);
				reset(conexion2);
	}
	@Test
	public void testEmpatan() {
		//Comprobamos las conexiones
		
				TicTacToeGame tictactoegame = new TicTacToeGame();
				
				Connection conexion1 = mock(Connection.class);
				Connection conexion2 = mock(Connection.class);
				
				tictactoegame.addConnection(conexion1);
				tictactoegame.addConnection(conexion2);
				
				Player jugador1 = new Player(1, "x", "jugador1");
				Player jugador2 = new Player(2, "o", "jugador2");
				
				List<Player> arrayJugadores = new CopyOnWriteArrayList<>();
				arrayJugadores.add(jugador1);
				
				tictactoegame.addPlayer(jugador1);
				verify(conexion1).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
				verify(conexion2).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
				
				arrayJugadores.add(jugador2);
				tictactoegame.addPlayer(jugador2);
				verify(conexion1, times(2)).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
				verify(conexion2, times(2)).sendEvent(eq(EventType.JOIN_GAME), eq(arrayJugadores));
				
				
				//Comprobamos los turnos
				//Ambas conexiones para el jugador1
				verify(conexion1).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
				verify(conexion2).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
				
				reset(conexion1);
				reset(conexion2);
				
				
				//hago una jugada
				tictactoegame.mark(0); //j1
				tictactoegame.mark(2); //j2
				tictactoegame.mark(1); //j1
				tictactoegame.mark(3); //j2
				tictactoegame.mark(5); //j1
				tictactoegame.mark(4); //j2
				tictactoegame.mark(6); //j1
				tictactoegame.mark(7); //j2
				
				
				//verifico que se reciben los cambios de turno
				
				verify(conexion1, times(4)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
				verify(conexion1, times(4)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(1)));
				
				verify(conexion2, times(4)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(0)));
				verify(conexion2, times(4)).sendEvent(eq(EventType.SET_TURN),eq(arrayJugadores.get(1)));
				

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
				
				reset(conexion1);
				reset(conexion2);
	}		
}

