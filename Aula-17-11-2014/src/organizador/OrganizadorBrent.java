package organizador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import aluno.Aluno;

public class OrganizadorBrent implements IFileOrganizer{

	/**
	 * Canal de escrita e leitura no arquivo
	 */
	private FileChannel canal;
	
	/**
	 *  valor primo que corresponde ao tamanho da tabela.
	 */
	private final long VALOR_PRIMO = 8000009;

	/**
	 * Valor estatico usado para indicar que a consulta não retornou registro
	 */
	private static final long INEXISTENTE = -1;

	/**
	 * 
	 * @param arqName
	 *            Nome do arquivo a ser utilizado como fonte de dados
	 * @throws FileNotFoundException
	 */
	public OrganizadorBrent(String arqName) throws FileNotFoundException {
		// Cria ou acessa o arquivo no diretório passado
		File file = new File(arqName);

		// Representa um arquivo usado para armazenar os dados
		RandomAccessFile raf = new RandomAccessFile(file, "rw");

		this.canal = raf.getChannel();
	}

	
	@Override
	public boolean addReg(Aluno pAluno) {
		try {
			long pos = this.getPosition(pAluno.getMatricula());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Aluno getReg(int pMatricula) {
		return null;
	}

	@Override
	public Aluno delReg(int pMatricula) {
		return null;
	}
	
	/**
	 * - Calcula o Hash da Matricula.
	 * Hash[chave] = chave mod P 
	 * onde P é um valor primo que corresponde ao tamanho da tabela.
	 * @param ppMatricula
	 * @return
	 * @throws IOException 
	 */
	private long getHash(int pMatricula) throws IOException{
		return pMatricula % this.VALOR_PRIMO;
	}
	
	/**
	 * - Calcula o Incremento da Matricula.
	 *  Inc(chave) = (chave mod (P-2)) + 1 
	 *  onde P é um valor primo que corresponde ao tamanho da tabela.
	 * @param ppMatricula
	 * @return
	 */
	private long getIncremento(int pMatricula){
		return (pMatricula % (this.VALOR_PRIMO-2)) + 1;
	}
	
	/**
	 * Obtem a posição de um Aluno no arquivo
	 * 
	 * @param matricula
	 *            Matricula do aluno pesquisado
	 * @return pos posição do aluno no arquivo. Caso não seja encontrado aluno,
	 *         retorna o valor estatico de INEXISTENTE (-1)
	 * @throws IOException
	 */
	private long getPosition(int pMatricula) throws IOException {
		// gera valor do hash
		long hash = this.getHash(pMatricula);
		
		// aloca o aluno a partir da posição do hash
		ByteBuffer buff = this.alocarAluno(hash, 4);

		// deveria imprimir 0
		System.out.println(buff.getInt());
		
		return 0;
	}
	
	/**
	 * Aloca uma instância de Aluno ou parte dos atributos deste em um
	 * ByteBuffer para recuperar os valores gravados
	 * 
	 * @param pos
	 *            Posição apartir da qual ira obter os valores do aluno
	 * @param pBuffTamanho
	 *            Tamanho do buffer. Varia a depender do que pretende obter.
	 *            Para matricula, apenas 4, matricula e nome 4 + 50...
	 * @return buff ByteBuffer
	 * @throws IOException
	 */
	private ByteBuffer alocarAluno(long pos, int pBuffTamanho) throws IOException {
		ByteBuffer buff = ByteBuffer.allocate(pBuffTamanho);
		this.canal.read(buff, pos);
		buff.flip();
		return buff;
	}
}
