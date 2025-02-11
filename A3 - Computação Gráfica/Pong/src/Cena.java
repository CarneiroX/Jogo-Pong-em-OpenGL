import java.util.Locale;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.gl2.GLUT;
import textura.Textura;

public class Cena implements GLEventListener {
	private GL2 gl;
	private GLUT glut;
	private float proporcao;
	private float xTransBola = 0;
	private float yTransBola = 1f;
	private char xDirecao;
	private char yDirecao = 'd';
	private float AnimacaoVida = 0;
	public boolean JogoPausado = false;
	public int Fasejogo = 0;
	private float VelocidadeJogo = 0.03f;
	public float BarraMoviJog = 0;
	private int PontuacaoUserJog = 0;
	private int VidasJog = 5;
	private int tonalizacao = GL2.GL_SMOOTH;
	private float angulo = 0;
	public float limite;
	float xMin, xMax, yMin, yMax, zMin, zMax;
	public boolean triangulo = false;
	private Textura textura;
	private int totalTextura = 1;
	public static final String Bedrock = "imagens/bedrock.png";
	public static final String Slime = "imagens/slime.png";
	public static final String Olho = "imagens/olho.png";
	public static final String Paisagem = "imagens/paisagem.jpg";
	public static final String Menu = "imagens/menu.png";
	public static final String Luta = "imagens/luta.png";
	public static final String Morte = "imagens/morte.jpg";
	public int filtro = GL2.GL_LINEAR;
	public int wrap = GL2.GL_REPEAT;
	public int modo = GL2.GL_MODULATE;
	public String texto;

	@Override
	public void init(GLAutoDrawable drawable) {
		aleatoriedadeBola();

		GL2 gl = drawable.getGL().getGL2();

		angulo = 0;
		limite = 1;
		texto = " GL_DECAL";
		texto = " GL_DECAL";
		textura = new Textura(totalTextura);
		gl.glEnable(GL2.GL_DEPTH_TEST);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();
		glut = new GLUT();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		switch (Fasejogo) {
			case 0:
				IniciaMenu();
				break;
			case 1:
				IniciaFaseUm();
				break;
			case 2:
				IniciaFaseDois();
				break;
			case 3:
				FimDeJogo();
				break;
		}

		iluminacaoDifusa(gl);
		iluminacaoAmbiente();
		ligaLuz(gl);
		gl.glFlush();

	}

	public void aleatoriedadeBola() {
		double xRandom = -0.8f + Math.random() * 1.6f;
		if (xRandom > 0) {
			xDirecao = 'r';
		} else {
			xDirecao = 'l';
		}
		xTransBola = Float.valueOf(String.format(Locale.US, "%.2f", xRandom));
	}

	public void ReiniciaJogo() {
		xTransBola = 0;
		yTransBola = 1f;
		yDirecao = 'd';
		JogoPausado = false;
		Fasejogo = 0;
		VelocidadeJogo = 0.028f;
		BarraMoviJog = 0;
		PontuacaoUserJog = 0;
		VidasJog = 5;
	}

	public void IniciaMenu() {
		String size = "big";
		float left = -0.3f;
		float begin = 0.8f;

		DesenhaTexto(left, begin -= 0.1f, size, "                                       Pong OpenGL                                     ");
		DesenhaTexto(left, begin -= 0.06f, size, "| ----------------------------------------- |");
		DesenhaTexto(left, begin -= 0.06f, size, "| O Objetivo do jogo é alcançar a maior pontuação possível ");
		DesenhaTexto(left, begin -= 0.06f, size, "| através da rebatida da bola. ");
		DesenhaTexto(left, begin -= 0.07f, size, "| ----------------------------------------- |");
		DesenhaTexto(left, begin -= 0.09f, size, "| # Comandos: ");
		DesenhaTexto(left, begin -= 0.08f, size, "| - Mover Bastão = [<] [>] ou [A] [D] ");
		DesenhaTexto(left, begin -= 0.06f, size, "| - Pausar Jogo = [P] ");
		DesenhaTexto(left, begin -= 0.06f, size, "| - Retornar para a Tela Inicial = [X] ");
		DesenhaTexto(left, begin -= 0.06f, size, "| - Fechar o Jogo = [Esc] ");
		DesenhaTexto(left, begin -= 0.07f, size, "| ----------------------------------------- |");
		DesenhaTexto(left, begin -= 0.09f, size, "| # Regras: ");
		DesenhaTexto(left, begin -= 0.08f, size, "| - A cada rebatida da bolinha, são computados 20 Pontos ");
		DesenhaTexto(left, begin -= 0.06f, size, "| - Acumulando 200 Pontos avança para a Segunda Fase ");
		DesenhaTexto(left, begin -= 0.06f, size, "| - Na Segunda Fase os Pontos são INFINITOS! ");
		DesenhaTexto(left, begin -= 0.07f, size, "| ----------------------------------------- |");
		DesenhaTexto(left, begin -= 0.09f, size, "| PRESSIONE [S] PARA INICIAR O JOGO ");

		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, Menu, 0);

		gl.glBegin (GL2.GL_QUADS );
		gl.glColor3f(1, 1, 1);
		gl.glTexCoord2f(0, limite);      gl.glVertex2f(-1, 1);
		gl.glTexCoord2f(limite, limite); gl.glVertex2f(1, 1);
		gl.glTexCoord2f(limite, 0);      gl.glVertex2f(1, -1);
		gl.glTexCoord2f(0, 0);           gl.glVertex2f(-1, -1);
		gl.glEnd();
		textura.desabilitarTextura(gl, 0);

	}

	public void IniciaFaseUm() {
		if (!JogoPausado) {
			FisicaBola();
		} else {
			DesenhaTexto(-0.1f, 0, "big", "JOGO PAUSADO");
		}

		DesenhaBarra();
		DesenhaBola();

		if (PontuacaoUserJog == 200) {
			Fasejogo = 2;
		}
		if (VidasJog == 0) {
			Fasejogo = 3;
		}

		DesenhaTexto(0.8f, 0.9f, "big", "Pontuação: " + PontuacaoUserJog);

		for (int i = 1; i <= 5; i++) {
			if (VidasJog >= i)
				DesenhaVidas(0.1f * i, true);
			else
				DesenhaVidas(0.1f * i, false);
		}

		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, Luta, 0);

		gl.glBegin (GL2.GL_QUADS );
		gl.glColor3f(1, 1, 1);
		gl.glTexCoord2f(0, limite);      gl.glVertex2f(-1, 1);
		gl.glTexCoord2f(limite, limite); gl.glVertex2f(1, 1);
		gl.glTexCoord2f(limite, 0);      gl.glVertex2f(1, -1);
		gl.glTexCoord2f(0, 0);           gl.glVertex2f(-1, -1);
		gl.glEnd();
		textura.desabilitarTextura(gl, 0);

	}

	public void IniciaFaseDois() {
		VelocidadeJogo = 0.036f;
		if (!JogoPausado) {
			FisicaBola();
		} else {
			DesenhaTexto(-0.1f, 0, "big", "JOGO PAUSADO");
		}

		DesenhaBarra();
		DesenhaBola();
		DesenhaObjetoFase2();

		if (VidasJog == 0) {
			Fasejogo = 3;
		}

		DesenhaTexto(0.8f, 0.9f, "big", "Pontuação: " + PontuacaoUserJog);

		for (int i = 1; i <= 5; i++) {
			if (VidasJog >= i)
				DesenhaVidas(0.1f * i, true);
			else
				DesenhaVidas(0.1f * i, false);
		}

		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, Paisagem, 0);

		gl.glBegin (GL2.GL_QUADS );
		gl.glColor3f(1, 1, 1);
		gl.glTexCoord2f(0, limite);      gl.glVertex2f(-1, 1);
		gl.glTexCoord2f(limite, limite); gl.glVertex2f(1, 1);
		gl.glTexCoord2f(limite, 0);      gl.glVertex2f(1, -1);
		gl.glTexCoord2f(0, 0);           gl.glVertex2f(-1, -1);
		gl.glEnd();
		textura.desabilitarTextura(gl, 0);

	}

	public void DesenhaVidas(float pos, boolean filled) { // Vida
		gl.glPushMatrix();
		if (filled)
			gl.glColor3f(0.5f, 0, 0.5f); // Cor da Vida
		else
			gl.glColor3f(1, 1, 1); // Cor quando perde vida

		gl.glTranslatef(0.4f + pos, 0.8f, 0); // Posição da vida
		gl.glRotatef(AnimacaoVida, 0.05f, 0.08f, 1); //Animação de rotação
		AnimacaoVida += 0.7f;

		glut.glutSolidCylinder(0.04f,0.03f,6,10); // Forma da Vida
		gl.glPopMatrix();
	}

	public void FimDeJogo() {
		float begin = 0.6f;
		float left = -0.16f;
		DesenhaTexto(left, begin -= 0.06f, "big", "| ------------------ |");
		DesenhaTexto(left, begin -= 0.06f, "big", "          GAME   OVER 		");
		DesenhaTexto(left, begin -= 0.06f, "big", "| ------------------ |");
		DesenhaTexto(left, begin -= 0.07f, "big", "  Pontuação Final: " + PontuacaoUserJog);
		DesenhaTexto(left, begin -= 0.13f, "big", "  Y - Retornar ao Menu");
		DesenhaTexto(left, begin -= 0.07f, "big", "  K - Fechar o Jogo");

		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, Morte, 0);

		gl.glBegin (GL2.GL_QUADS );
		gl.glColor3f(1, 1, 1);
		gl.glTexCoord2f(0, limite);      gl.glVertex2f(-1, 1);
		gl.glTexCoord2f(limite, limite); gl.glVertex2f(1, 1);
		gl.glTexCoord2f(limite, 0);      gl.glVertex2f(1, -1);
		gl.glTexCoord2f(0, 0);           gl.glVertex2f(-1, -1);
		gl.glEnd();
		textura.desabilitarTextura(gl, 0);

	}

	public void DesenhaTexto(float x, float y, String size, String phrase) {
		gl.glColor3f(0.1f, 1, 0.8f);
		gl.glRasterPos2f(x, y);
		switch (size) {
			case "small":
				glut.glutBitmapString(GLUT.BITMAP_8_BY_13, phrase);
				break;
			case "big":
				glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, phrase);
		}
	}

	public boolean BolaBarra(float xTransBolaCorrigida) {
		float LimiteEsquerdaBarra = Float.valueOf(String.format(Locale.US, "%.1f", BarraMoviJog - 0.2f));
		float LimiteDireitaBarra = Float.valueOf(String.format(Locale.US, "%.1f", BarraMoviJog + 0.2f));

		if (LimiteEsquerdaBarra <= xTransBolaCorrigida && LimiteDireitaBarra >= xTransBolaCorrigida) {
			return true;
		}
		return false;
	}

	public boolean YBolaObjeto(float xObj, float yObj, float bLimite, float tLimite, float xPonto) {
		if (tLimite >= yObj && bLimite <= yObj && xObj == xPonto) {
			return true;
		}
		return false;
	}

	public boolean XBolaObjeto(float xObj, float yObj, float lLimite, float rLimite, float tLimite) {
		if (lLimite <= xObj && rLimite >= xObj && yObj == tLimite) {
			return true;
		}
		return false;
	}

	public void FisicaBola() {
		float xTransBolaCorrigida = Float.valueOf(String.format(Locale.US, "%.1f", xTransBola));
		float yTransBolaCorrigida = Float.valueOf(String.format(Locale.US, "%.1f", yTransBola));

		if (Fasejogo == 2 && xDirecao == 'l'
				&& YBolaObjeto(xTransBolaCorrigida, yTransBolaCorrigida, -0.1f, 0.5f, 0.2f)) {
			xDirecao = 'r';
		}
		if (Fasejogo == 2 && xDirecao == 'r'
				&& YBolaObjeto(xTransBolaCorrigida, yTransBolaCorrigida, -0.1f, 0.5f, -0.2f)) {
			xDirecao = 'l';
		} else if (xTransBolaCorrigida > -1f && xDirecao == 'l') {
			xTransBola -= VelocidadeJogo /2;
		} else if (xTransBolaCorrigida == -1f && xDirecao == 'l') {
			xDirecao = 'r';
		} else if (xTransBolaCorrigida < 1f && xDirecao == 'r') {
			xTransBola += VelocidadeJogo /2;
		} else if (xTransBolaCorrigida == 1f && xDirecao == 'r') {
			xDirecao = 'l';
		}
		if (Fasejogo == 2 && yDirecao == 'u'
				&& XBolaObjeto(xTransBolaCorrigida, yTransBolaCorrigida, -0.2f, 0.2f, -0.2f)) {
			yDirecao = 'd';
		} else if (Fasejogo == 2 && yDirecao == 'd'
				&& XBolaObjeto(xTransBolaCorrigida, yTransBolaCorrigida, -0.2f, 0.2f, 0.6f)) {
			yDirecao = 'u';
		} else if (yTransBolaCorrigida == -0.7f && yDirecao == 'd'
				&& BolaBarra(xTransBolaCorrigida)) {
			yDirecao = 'u';
			tonalizacao = tonalizacao == GL2.GL_SMOOTH ? GL2.GL_FLAT : GL2.GL_SMOOTH;
			PontuacaoUserJog += 20;
		} else if (yTransBolaCorrigida < 0.9f && yDirecao == 'u') {
			yTransBola += VelocidadeJogo;
		} else if (yTransBolaCorrigida == 0.9f && yDirecao == 'u') {
			yDirecao = 'd';
		} else if (yTransBolaCorrigida < -1f) {
			yTransBola = 1f;
			xTransBola = 0;
			VidasJog--;
			aleatoriedadeBola();
		} else {
			yTransBola -= VelocidadeJogo;
			tonalizacao = tonalizacao == GL2.GL_SMOOTH ? GL2.GL_FLAT : GL2.GL_SMOOTH;
		}
	}

	public void DesenhaObjetoFase2() {
		gl.glPushMatrix();
		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, Bedrock, 0);

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(1, 1, 1);
		gl.glTexCoord2f(0, limite);      gl.glVertex2f(-0.18f, 0.53f);
		gl.glTexCoord2f(limite, limite); gl.glVertex2f(0.18f, 0.53f);
		gl.glTexCoord2f(limite, 0);      gl.glVertex2f(0.18f, -0.13f);
		gl.glTexCoord2f(0, 0);           gl.glVertex2f(-0.18f, -0.13f);
		gl.glEnd();

		textura.desabilitarTextura(gl, 0);

		gl.glPopMatrix();
	}

	public void DesenhaBarra() {
		gl.glPushMatrix();
		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, Slime, 0);

		gl.glTranslatef(BarraMoviJog, 0, 0);
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(1f, 1f, 1f);

		gl.glTexCoord2f(0, limite);      gl.glVertex2f(-0.2f, -0.8f);
		gl.glTexCoord2f(limite, limite); gl.glVertex2f(0.2f, -0.8f);
		gl.glTexCoord2f(limite, 0);      gl.glVertex2f(0.2f, -0.9f);
		gl.glTexCoord2f(0, 0);           gl.glVertex2f(-0.2f, -0.9f);
		gl.glEnd();

		textura.desabilitarTextura(gl, 0);

		gl.glPopMatrix();
	}

	public void DesenhaBola() {
		gl.glPushMatrix();
		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, Olho, 0);

		gl.glTranslatef(xTransBola, yTransBola, 0);

		double limit = 2 * Math.PI;
		double i;
		double cX = 0;
		double cY = 0;
		double rX = 0.1f / proporcao;
		double rY = 0.1f;

		gl.glBegin(GL2.GL_POLYGON);
		for (i = 0; i < limit; i += 0.01) {
			gl.glTexCoord2f((float)Math.cos(i) / ((float)1.3 * proporcao) + ((float)1.97 * proporcao), (float)Math.sin(i) / ((float)1.3 * proporcao) + ((float)1.97 * proporcao));
			gl.glVertex2d(cX + rX * Math.cos(i), cY + rY * Math.sin(i));
		}
		gl.glEnd();

		textura.desabilitarTextura(gl, 0);

		gl.glPopMatrix();

	}
	public void iluminacaoAmbiente() {
		float luzAmbiente[] = {0.8f, 0.7f, 0.6f, 1.0f}; //cor
		float posicaoLuz[] = {0.0f, 0.0f, 100.0f, 1.0f}; //pontual

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
	}

	public void iluminacaoDifusa(GL2 gl) {
		float luzDifusa[] = {1.0f, 1.0f, 1.0f, 1.0f}; //cor
		float posicaoLuz[] = {-50.0f, 0.0f, 100.0f, 0.0f}; //1.0 pontual

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, luzDifusa, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
	}

	public void ligaLuz(GL2 gl) {
		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glShadeModel(GL2.GL_SMOOTH);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		proporcao = (float) width / height;
		gl.glOrtho(-1, 1, -1, 1, -1, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

}