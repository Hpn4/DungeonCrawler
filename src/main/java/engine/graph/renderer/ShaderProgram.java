package engine.graph.renderer;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL32;

import loader.map.Utils;

/**
 * Stock les shader rattaché au programme.
 * 
 * Permet de créer des uniform et de les éditer.
 * 
 * Cette class supporte le vertex shader, le geometry shader et le fragment
 * shader
 * 
 * @author Hpn4
 *
 */
public class ShaderProgram {

	/** L'id du programme, les shader lui sont attaché */
	private final int programId;

	/** L'id du vertex shader */
	private final int vertexShaderId;

	/** L'id du geometry shader */
	private final int geometryShaderId;

	/** L'id du fragment shader */
	private final int fragmentShaderId;

	/** Map ayant comme clé le nom d'un uniform et en valeur son emplacement */
	private final Map<String, Integer> uniforms;

	private final String shaderName;

	/**
	 * Créer un programme avec un vertex shader {@code shaderName}.vert et un
	 * fragment shader {@code shaderName}.frag
	 * 
	 * @param shaderName Le nom du shader a créer
	 * @throws Exception Si les fichiers n'existe pas, sont illisible ou si la
	 *                   compilation échoue
	 */
	public ShaderProgram(final String shaderName) throws Exception {
		this(shaderName + ".vert", null, shaderName + ".frag");
	}

	/**
	 * Créer un programme avec un vertex shader {@code vertexName}, un geometry
	 * shader {@code geometryName} et un fragment shader {@code fragmentName}
	 * 
	 * @param vertexName   Le nom du fichier du vertex shader
	 * @param geometryName Le nom du fichier du geometry shader
	 * @param fragmentName Le nom du fichier du fragment shader
	 * @throws Exception Si les fichiers n'existe pas, sont illisible ou si la
	 *                   compilation échoue
	 * 
	 * @see {@link #loadShader(String, int)}
	 */
	public ShaderProgram(final String vertexName, final String geometryName, final String fragmentName)
			throws Exception {
		programId = glCreateProgram();
		if (programId == 0)
			throw new Exception("Could not create Shader : " + vertexName);
		uniforms = new HashMap<>();

		shaderName = vertexName.substring(0, vertexName.lastIndexOf("."));

		vertexShaderId = loadShader(vertexName, GL_VERTEX_SHADER);
		geometryShaderId = loadShader(geometryName, GL_GEOMETRY_SHADER);
		fragmentShaderId = loadShader(fragmentName, GL_FRAGMENT_SHADER);

		link();
	}

	/**
	 * Charge en mémoire le shader {@code shaderName}, le créer en fonction de son
	 * type {@code shaderType}, le compile et l'attache au programme.
	 * 
	 * @param shaderName Le nom du fichier du shader
	 * @param shaderType Le type du shader un de :<br>
	 *                   <table>
	 *                   <tr>
	 *                   <td>{@link GL20C#GL_VERTEX_SHADER VERTEX_SHADER}</td>
	 *                   <td>{@link GL20C#GL_FRAGMENT_SHADER FRAGMENT_SHADER}</td>
	 *                   <td>{@link GL32#GL_GEOMETRY_SHADER GEOMETRY_SHADER}</td>
	 *                   </tr>
	 *                   </table>
	 * @return L'id du shader compilé
	 * @throws Exception si le fichier n'éxiste pas, est illisible. Ou si la
	 *                   compilation échoue
	 */
	private int loadShader(final String shaderName, final int shaderType) throws Exception {
		if (shaderName != null) {
			final String shaderCode = Utils.read("shaders/" + shaderName);

			final int shaderId = glCreateShader(shaderType);
			if (shaderId == 0)
				throw new Exception("Error creating shader. Type: " + shaderType);

			glShaderSource(shaderId, shaderCode);
			glCompileShader(shaderId);

			if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
				System.err.println("Error in shader file : " + shaderName);
				throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
			}

			glAttachShader(programId, shaderId);

			return shaderId;
		}

		return 0;
	}

	/*
	 ******************************************
	 * * * CREATE UNIFORM * * *
	 ******************************************
	 */

	/**
	 * Créer tous les uniform présent dans la liste d'uniform {@code uniformsName}
	 * 
	 * @param uniformsName La liste d'uniform à créer
	 * @throws Exception Si l'uniform n'éxiste pas dans le programme
	 * 
	 * @see #createUniform(String)
	 */
	public void createUniform(final String... uniformsName) throws Exception {
		for (int i = 0, c = uniformsName.length; i < c; i++)
			createUniform(uniformsName[i]);
	}

	/**
	 * Créer un tableau d'uniform {@code uniformName} de longueur {@code count}
	 * 
	 * @param uniformName Le nom du tableau d'uniform
	 * @param count       La longueur du tableau
	 * @throws Exception Si l'uniform n'éxiste pas dans le programme
	 * 
	 * @see #createUniform(String)
	 */
	public void createUniform(final String uniformName, final int count) throws Exception {
		for (int i = 0; i < count; i++)
			createUniform(uniformName + "[" + i + "]");
	}

	/**
	 * Créer l'uniform {@code uniformName}
	 * 
	 * @param uniformName Le nom de l'uniform a créer
	 * @throws Exception Si l'uniform n'existe pas dans le programmes ( il n'éxiste
	 *                   pas dans les shader )
	 */
	public void createUniform(final String uniformName) throws Exception {
		final int uniformLocation = glGetUniformLocation(programId, uniformName);

		if (uniformLocation < 0)
			throw new Exception("Could not find uniform:" + uniformName);
		uniforms.put(uniformName, uniformLocation);
	}

	/*
	 ******************************************
	 * * * SET UNIFORM * * *
	 ******************************************
	 */

	/**
	 * Définie l'uniform {@code uniformName} à l'objet Matrix3f {@code value}
	 * 
	 * @param uniformName Le nom de l'uniform
	 * @param value       L'objet Matrix3f à définir
	 * 
	 * @see #createUniform(String)
	 * @see Matrix3f
	 */
	public void setMatrix3f(final String uniformName, final Matrix3f value) {
		glUniformMatrix3fv(uniforms.get(uniformName), false, value.get(new float[12]));
	}

	/**
	 * Définie l'uniform {@code uniformName} à l'objet Matrix4f {@code value}
	 * 
	 * @param uniformName Le nom de l'uniform
	 * @param value       L'objet Matrix4f à définir
	 * 
	 * @see #createUniform(String)
	 * @see Matrix4f
	 */
	public void setMatrix4f(final String uniformName, final Matrix4f value) {
		glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(new float[16]));
	}

	/**
	 * Définie le tableau d'uniform {@code uniformName} à l'index {@code index} à
	 * l'objet Matrix4f {@code value}
	 * 
	 * @param uniformName Le nom du tableau d'uniform
	 * @param value       L'objet Matrix4f à définir
	 * @param index       L'index a définir dans la tableau
	 * 
	 * @see #setMatrix4f(String, Matrix4f)
	 * @see Matrix4f
	 */
	public void setMatrix4fAtIndex(final String uniformName, final Matrix4f value, final int index) {
		setMatrix4f(uniformName + "[" + index + "]", value);
	}

	/**
	 * Définie le tableau d'uniform {@code uniformName} à la liste d'objet Matrix4f
	 * {@code matrices}
	 * 
	 * @param uniformName Le nom de l'uniform
	 * @param matrices    La liste d'objet Matrix4f à définir
	 * 
	 * @see Matrix4f
	 */
	public void setMatrices4f(final String uniformName, final Matrix4f[] matrices) {
		final int length = matrices != null ? matrices.length : 0;
		final float[] values = new float[16 * length];

		for (int i = 0; i < length; i++)
			matrices[i].get(values, 16 * i);
		glUniformMatrix4fv(uniforms.get(uniformName), false, values);
	}

	/**
	 * Définie l'uniform {@code uniformName} au nombre entier {@code value}
	 * 
	 * @param uniformName Le nom de l'uniform
	 * @param value       Le nombre entier à définir
	 * 
	 * @see #createUniform(String)
	 */
	public void setInt(final String uniformName, final int value) {
		glUniform1i(uniforms.get(uniformName), value);
	}

	/**
	 * Définie l'uniform {@code uniformName} au nombre à virgule {@code value}
	 * 
	 * @param uniformName Le nom de l'uniform
	 * @param value       Le nombre à virgule à définir
	 * 
	 * @see #createUniform(String)
	 */
	public void setFloat(final String uniformName, final float value) {
		glUniform1f(uniforms.get(uniformName), value);
	}

	/**
	 * Définie le tableau d'uniform {@code uniformName} à l'index {@code index} au
	 * nombre à virgule {@code value}
	 * 
	 * @param uniformName Le nom du tableau d'uniform
	 * @param value       Le nombre à virgule à définir
	 * @param index       L'index a définir dans la tableau
	 * 
	 * @see #setFloat(String, float)
	 */
	public void setFloatAtIndex(final String uniformName, final float value, final int index) {
		setFloat(uniformName + "[" + index + "]", value);
	}

	/**
	 * Définie l'uniform {@code uniformName} au boolean {@code value}, l'uniform est
	 * un nombre entier définie en fonction de la valeur du boolean.
	 * {@code value == true ? 1 : 0}
	 * 
	 * @param uniformName Le nom de l'uniform
	 * @param value       Le boolean à définir
	 * 
	 * @see #setInt(String, int)
	 * @see #createUniform(String)
	 */
	public void setBoolean(final String uniformName, final boolean value) {
		setInt(uniformName, value ? 1 : 0);
	}

	/**
	 * Définie l'uniform {@code uniformName} au boolean {@code value}, l'uniform est
	 * un nombre à virgule définie en fonction de la valeur du boolean.
	 * {@code value == true ? 1f : 0f}
	 * 
	 * @param uniformName Le nom de l'uniform
	 * @param value       Le boolean à définir
	 * 
	 * @see #setFloat(String, float)
	 * @see #createUniform(String)
	 */
	public void setBooleanf(final String uniformName, final boolean value) {
		setFloat(uniformName, value ? 1f : 0f);
	}

	/**
	 * Définie l'uniform {@code uniformName} à l'objet Vector2f {@code value}
	 * 
	 * @param uniformName Le nom de l'uniform
	 * @param value       L'objet Vector2f à définir
	 * 
	 * @see #setVector2f(String, float, float)
	 * @see #createUniform(String)
	 * @see Vector2f
	 */
	public void setVector2f(final String uniformName, final Vector2f value) {
		setVector2f(uniformName, value.x, value.y);
	}

	/**
	 * Définie l'uniform {@code uniformName} à un vecteur a 2 dimension : {@code x}
	 * et {@code y}
	 * 
	 * @param uniformName Le nom de l'uniform
	 * @param x           La valeur x du vecteur
	 * @param y           La valeur y du vecteur
	 * 
	 * @see #createUniform(String)
	 */
	public void setVector2f(final String uniformName, final float x, final float y) {
		glUniform2f(uniforms.get(uniformName), x, y);
	}

	/**
	 * Définie le tableau d'uniform {@code uniformName} à l'index {@code index} à
	 * l'objet Vector2f {@code value}
	 * 
	 * @param uniformName Le nom du tableau d'uniform
	 * @param value       L'objet Vector2f à définir
	 * @param index       L'index a définir dans la tableau
	 * 
	 * @see #setVector2f(String, Vector2f)
	 * @see Vector2f
	 */
	public void setVector2fAtIndex(final String uniformName, final Vector2f value, final int index) {
		setVector2f(uniformName + "[" + index + "]", value);
	}

	/**
	 * Définie l'uniform {@code uniformName} à l'objet Vector3f {@code value}
	 * 
	 * @param uniformName Le nom de l'uniform
	 * @param value       L'objet Vector3f à définir
	 * 
	 * @see #createUniform(String)
	 * @see Vector3f
	 */
	public void setVector3f(final String uniformName, final Vector3f value) {
		glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
	}

	/**
	 * Définie l'uniform {@code uniformName} au valeur {@code x}, {@code y} et
	 * {@code z}
	 * 
	 * @param uniformName Le nom de l'uniform
	 * @param x           Le composant x
	 * @param y           Le composant y
	 * @param z           Le composant z
	 * 
	 * @see #createUniform(String)
	 * @see Vector3f
	 */
	public void setVector3f(final String uniformName, final float x, final float y, final float z) {
		glUniform3f(uniforms.get(uniformName), x, y, z);
	}

	/**
	 * Définie le tableau d'uniform {@code uniformName} à l'index {@code index} à
	 * l'objet Vector3f {@code value}
	 * 
	 * @param uniformName Le nom du tableau d'uniform
	 * @param value       L'objet Vector3f à définir
	 * @param index       L'index a définir dans la tableau
	 * 
	 * @see #setVector3f(String, Vector3f)
	 * @see Vector3f
	 */
	public void setVector3fAtIndex(final String uniformName, final Vector3f value, final int index) {
		setVector3f(uniformName + "[" + index + "]", value);
	}

	/**
	 * Définie l'uniform {@code uniformName} à l'objet Vector4f {@code value}
	 * 
	 * @param uniformName Le nom de l'uniform
	 * @param value       L'objet Vector4f à définir
	 * 
	 * @see #createUniform(String)
	 * @see Vector4f
	 */
	public void setVector4f(final String uniformName, final Vector4f value) {
		glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
	}

	/**
	 * Attache les shaders au programme
	 * 
	 * @throws Exception
	 */
	public void link() throws Exception {
		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) == 0)
			throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));

		if (vertexShaderId != 0)
			glDetachShader(programId, vertexShaderId);

		if (geometryShaderId != 0)
			glDetachShader(programId, geometryShaderId);

		if (fragmentShaderId != 0)
			glDetachShader(programId, fragmentShaderId);
	}

	public void validate() {
		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0)
			System.err.println("[SHADER " + shaderName + "] " + glGetProgramInfoLog(programId, 1024));
	}

	/**
	 * Charge ce programme dans la VRAM pour qu'il soit execute
	 */
	public void bind() {
		glUseProgram(programId);
	}

	/**
	 * Retire ce programme de la VRAM
	 */
	public void unbind() {
		glUseProgram(0);
	}

	/**
	 * Supprime toutes les ressources utilisée, le programme ainsi que les shaders
	 */
	public void cleanup() {
		unbind();

		// Supprime le programme
		if (programId != 0)
			glDeleteProgram(programId);

		// Supprime tous les shader si ils ont ete creer.
		if (vertexShaderId != 0)
			glDeleteShader(vertexShaderId);

		if (geometryShaderId != 0)
			glDeleteShader(geometryShaderId);

		if (fragmentShaderId != 0)
			glDeleteShader(fragmentShaderId);
	}
}