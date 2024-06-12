import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        int a = 56;
        int b = 8;
        int soma = a + b;
        System.out.println("A soma de " + a + " e " + b + " é: " + soma);
        int numero = 7;
        if (numero % 2 == 0) {
            System.out.println(numero + " é par.");
        } else {
            System.out.println(numero + " é ímpar.");
        }

        for (int i = 1; i <= 9; i++) {
            System.out.println("Contagem: " + i);
        }

        int[] numeros = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int numeroArray : numeros) {
            System.out.println("Número: " + numeroArray);
        }
        Doador doador1 = new Doador("Joao", "joao@example.com");
        Doador doador2 = new Doador("Maria", "maria@example.com");
        SistemaGerenciamentoDoacoes sistema = new SistemaGerenciamentoDoacoes();
        sistema.registrarDoacao("Alimentos", 8, new Date(), doador1);
        sistema.registrarDoacao("Roupas", 56, new Date(), doador2);
        System.out.println("Total de Doações: " + sistema.calcularTotalDoacoes());
        sistema.gerarRelatorio("relatorio_doacoes.txt");
        System.out.println("Relatorio de doações gerado em relatorio_doacoes.txt");
        System.out.println("Total de Alimentos doados: " + sistema.getTotalPorTipo("Alimentos"));
        System.out.println("Total de Roupas doadas: " + sistema.getTotalPorTipo("Roupas"));

        try {
            int resultado = dividir(56, 8);
            System.out.println("Resultado: " + resultado);
        } catch (ArithmeticException e) {
            System.out.println("Erro: Divisão por zero! " + e.getMessage()); 
        }

    }

    public static int dividir(int a, int b) {
        return a / b;
    }

    public static void escreverArquivo(String caminho, String conteudo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
            writer.write(conteudo);
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public static void lerArquivo(String caminho) {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}

class Doador {
    private String nome;
    private String contato;

    public Doador(String nome, String contato) {
        this.nome = nome;
        this.contato = contato;
    }

    public String getNome() {
        return nome;
    }

    public String getContato() {
        return contato;
    }
}

class Doacao {
    private String tipo;
    private int quantidade;
    private Date data;
    private Doador doador;

    public Doacao(String tipo, int quantidade, Date data, Doador doador) {
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.data = data;
        this.doador = doador;
    }

    public String getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Date getData() {
        return data;
    }

    public Doador getDoador() {
        return doador;
    }
}

class SistemaGerenciamentoDoacoes {
    private ArrayList<Doacao> listaDoacoes;
    private Map<String, Integer> totaisPorTipo;

    public SistemaGerenciamentoDoacoes() {
        this.listaDoacoes = new ArrayList<>();
        this.totaisPorTipo = new HashMap<>();
    }

    public void registrarDoacao(String tipo, int quantidade, Date data, Doador doador) {
        Doacao novaDoacao = new Doacao(tipo, quantidade, data, doador);
        listaDoacoes.add(novaDoacao);

        if (totaisPorTipo.containsKey(tipo)) {
            int totalAtual = totaisPorTipo.get(tipo);
            totaisPorTipo.put(tipo, totalAtual + quantidade);
        } else {
            totaisPorTipo.put(tipo, quantidade);
        }
    }

    public int calcularTotalDoacoes() {
        int total = 0;
        for (Doacao doacao : listaDoacoes) {
            total += doacao.getQuantidade();
        }
        return total;
    }

    public void gerarRelatorio(String caminhoArquivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            writer.write("Relatorio de Doacoes:\n");
            for (Doacao doacao : listaDoacoes) {
                String dataFormatada = dateFormat.format(doacao.getData());
                writer.write("Tipo: " + doacao.getTipo() + ", Quantidade: " + doacao.getQuantidade() + ", Data: " + dataFormatada + ", Doador: " + doacao.getDoador().getNome() + ", Contato: " + doacao.getDoador().getContato() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public int getTotalPorTipo(String tipo) {
        return totaisPorTipo.containsKey(tipo) ? totaisPorTipo.get(tipo) : 0;
    }
}
