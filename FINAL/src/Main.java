import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SistemaGerenciamentoDoacoes sistema = new SistemaGerenciamentoDoacoes();

        while (true) {
            System.out.println("=== Sistema de Gerenciamento de Doações ===");
            System.out.println("1. Registrar doação");
            System.out.println("2. Exibir total de doações");
            System.out.println("3. Gerar relatório de doações");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcao) {
                case 1:
                    registrarDoacao(scanner, sistema);
                    break;
                case 2:
                    exibirTotalDoacoes(scanner, sistema);
                    break;
                case 3:
                    sistema.gerarRelatorio("relatorio_doacoes.txt");
                    System.out.println("Relatório de doações gerado em relatorio_doacoes.txt");
                    break;
                case 4:
                    scanner.close();
                    System.out.println("Saindo do sistema. Obrigado!");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void registrarDoacao(Scanner scanner, SistemaGerenciamentoDoacoes sistema) {
        System.out.print("Informe o nome do doador: ");
        String nomeDoador = scanner.nextLine();
        System.out.print("Digite o contato do doador: (e-mail, celular) ");
        String contatoDoador = scanner.nextLine();
        Doador doador = new Doador(nomeDoador, contatoDoador);

        System.out.print("Digite o tipo de doação (dinheiro, alimentos, roupas, etc.): ");
        String tipoDoacao = scanner.nextLine();
        System.out.print("Digite a quantidade doada: ");
        int quantidadeDoacao;

        try {
            quantidadeDoacao = Integer.parseInt(scanner.nextLine());
            if (quantidadeDoacao <= 0) {
                System.out.println("Erro: A quantidade deve ser maior que zero.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: A quantidade deve ser um número válido.");
            return;
        }

        if (nomeDoador.isEmpty() || contatoDoador.isEmpty() || tipoDoacao.isEmpty()) {
            System.out.println("Erro: Todos os campos são obrigatórios.");
        } else {
            sistema.registrarDoacao(tipoDoacao, quantidadeDoacao, new Date(), doador);
            System.out.println("Doação registrada com sucesso!");
        }
    }

    private static void exibirTotalDoacoes(Scanner scanner, SistemaGerenciamentoDoacoes sistema) {
        System.out.println("Total de Doações: " + sistema.calcularTotalDoacoes());
        System.out.print("Digite o tipo de doação para filtrar (ou pressione Enter para exibir todos): ");
        String filtroTipo = scanner.nextLine();
        if (filtroTipo.isEmpty()) {
            sistema.exibirTotalPorTipo();
        } else {
            System.out.println("Total de " + filtroTipo + " doados: " + sistema.getTotalPorTipo(filtroTipo));
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

    public void exibirTotalPorTipo() {
        for (Map.Entry<String, Integer> entry : totaisPorTipo.entrySet()) {
            System.out.println("Tipo: " + entry.getKey() + ", Total: " + entry.getValue());
        }
    }

    public void gerarRelatorio(String caminhoArquivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            writer.append("Relatorio de Doacoes:\n");
            writer.newLine();
            int contador = 1;
            for (Doacao doacao : listaDoacoes) {
                String dataFormatada = dateFormat.format(doacao.getData());
                writer.append(contador + ". Tipo: " + doacao.getTipo());
                writer.newLine();
                writer.append("   Quantidade: " + doacao.getQuantidade());
                writer.newLine();
                writer.append("   Data: " + dataFormatada);
                writer.newLine();
                writer.append("   Doador: " + doacao.getDoador().getNome());
                writer.newLine();
                writer.append("   Contato: " + doacao.getDoador().getContato());
                writer.newLine();
                writer.newLine();
                contador++;
            }
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public int getTotalPorTipo(String tipo) {
        return totaisPorTipo.containsKey(tipo) ? totaisPorTipo.get(tipo) : 0;
    }
}
