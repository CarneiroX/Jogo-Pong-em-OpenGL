package textura;

import com.jogamp.opengl.GL2;
import java.io.File;
import java.io.IOException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Textura {
    private Texture vetTextures[];
    private float width;
    private float height;
    private int filtro;
    private int modo;
    private int wrap;
    private boolean automatica;
    public Textura(int totalTextura) {
        vetTextures = new Texture[totalTextura];
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
    public void setFiltro(int filtro) {
        this.filtro = filtro;
    }
    public float getFiltro() {
        return filtro;
    }
    public void setModo(int modo) {
        this.modo = modo;
    }
    public float getModo() {
        return modo;
    }
    public void setWrap(int wrap) {
        this.wrap = wrap;
    }
    public float getWrap() {
        return wrap;
    }
    public void setAutomatica(boolean automatica){
        this.automatica = automatica;
    }
    public boolean getAutomatica(){
        return automatica;
    }

    public void gerarTextura(GL2 gl, String fileName, int indice) {
        carregarTextura(fileName, indice);
        Texture tex = vetTextures[indice];
        tex.bind(gl);
        tex.enable(gl);

        tex.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, filtro);
        tex.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, filtro);

        tex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, wrap);
        tex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, wrap);

        gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, modo);

    }

    private void carregarTextura(String fileName, int indice) {
        Texture tex = null;
        try {
            tex = TextureIO.newTexture(new File(fileName), true);
        } catch (IOException e) {
            System.out.println("\n=============\nErro na leitura do arquivo "
                    + fileName + "\n=============\n");
        }

        this.width = tex.getWidth();
        this.height = tex.getHeight();

        vetTextures[indice] = tex;
    }

    public void desabilitarTexturaAutomatica(GL2 gl) {
        //desabilita o uso de textura
        gl.glDisable(GL2.GL_TEXTURE_GEN_S);
        gl.glDisable(GL2.GL_TEXTURE_GEN_T);
    }

   public void desabilitarTextura(GL2 gl, int indice) {
        Texture tex = vetTextures[indice];
        tex.disable(gl); //desabilita a textura
        tex.destroy(gl); //destroi os recursos
        if(this.automatica)
            desabilitarTexturaAutomatica(gl);
    }
}