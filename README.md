## Melhorias

Foi implementado testes com Mockito e JUnit para garantir que a refatoração não alterasse o comportamento do sistema.

O método run foi dividido em métodos menores (executarFaseDeTestes, executarFaseDeDeploy), aplicando o Princípio de Responsabilidade Única.

As condicionais confusas foram substituídas por variáveis booleanas descritivas, eliminando números mágicos e strings soltas.

A lógica de notificação foi isolada, protegendo o fluxo principal de detalhes de implementação do envio de e-mail.

Foi criado um método específico para definir mensagens, separando a lógica de negócio da lógica de apresentação.