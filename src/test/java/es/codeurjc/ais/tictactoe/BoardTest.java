package es.codeurjc.ais.tictactoe;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {
private Board board;
	
	@BeforeEach
	public void setUp() {
		board = new Board();
	}
	
	@Test
	public void testGanaJugador1() {
			
		String jugador1 = "x";
		String jugador2 = "o";
		
		int[] posicionesGanador = { 0, 3, 6 };
		int[] posicionesJ2 = {1, 2, 7};
		
		for (int pos : posicionesGanador) {
			board.getCell(pos).value = jugador1;	
		}
		
		for (int pos : posicionesJ2) {
			board.getCell(pos).value = jugador2;	
		}
		
		int [] celdas = board.getCellsIfWinner(jugador1);
		assertArrayEquals(celdas, posicionesGanador);
	}
	
	@Test
	public void testGanaJugador2() {
		
		String jugador1 = "x";
		String jugador2 = "o";

		int[] posicionesJ1 = {1, 6, 7}; 
		int[] posicionesGanador = { 2, 5, 8 };
		
		
		for (int pos : posicionesJ1) {
			board.getCell(pos).value = jugador1;	
		}
		for (int pos : posicionesGanador) {
			board.getCell(pos).value = jugador2;	
		}
		
		int [] celdas = board.getCellsIfWinner(jugador2);
		assertArrayEquals(celdas, posicionesGanador);
	}
	
	@Test
	public void testEmpatanJugadores() {
		String jugador1 = "x";
		String jugador2 = "o";
		
		int[] posicionesJ1 = { 0, 3, 7, 2, 8};
		int[] posicionesJ2 = { 6, 4, 1, 5};		
		
		for (int pos : posicionesJ1) {
			board.getCell(pos).value = jugador1;	
		}
		for (int pos : posicionesJ2) {
			board.getCell(pos).value = jugador2;	
		}
		
		assertTrue(board.checkDraw());
	}
}
