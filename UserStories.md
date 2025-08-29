# ADA LocateCar - User Stories e Critérios de Aceitação

## 📋 Épico: Gestão de Veículos

### US001 - Cadastrar Veículo
**Como** administrador da locadora  
**Quero** cadastrar um novo veículo no sistema  
**Para** disponibilizá-lo para locação

**Critérios de Aceitação:**
- ✅ **CA001.1** - O sistema deve permitir inserir: placa, modelo, marca, ano, tipo (PEQUENO, MEDIO, SUV), cor
- ✅ **CA001.2** - A placa deve ser única no sistema (RN1)
- ✅ **CA001.3** - O tipo do veículo deve ser obrigatoriamente: PEQUENO, MEDIO ou SUV (RN2)
- ✅ **CA001.4** - Todos os campos obrigatórios devem ser preenchidos
- ✅ **CA001.5** - O sistema deve exibir mensagem de sucesso ao cadastrar
- ✅ **CA001.6** - O sistema deve exibir erro se tentar cadastrar placa já existente

**Cenários de Teste:**
- **Cenário 1**: Cadastro com sucesso
  - Dado que informo todos os dados válidos
  - Quando confirmo o cadastro
  - Então o veículo é salvo e mensagem de sucesso é exibida

- **Cenário 2**: Placa duplicada
  - Dado que informo uma placa já cadastrada
  - Quando confirmo o cadastro
  - Então o sistema exibe erro "Placa já cadastrada"

---

### US002 - Alterar Veículo
**Como** administrador da locadora  
**Quero** alterar dados de um veículo cadastrado  
**Para** manter as informações atualizadas

**Critérios de Aceitação:**
- ✅ **CA002.1** - O sistema deve permitir buscar veículo pela placa
- ✅ **CA002.2** - Deve ser possível alterar: modelo, marca, ano, tipo, cor
- ✅ **CA002.3** - A placa não pode ser alterada (identificador único)
- ✅ **CA002.4** - As mesmas validações do cadastro se aplicam
- ✅ **CA002.5** - Sistema deve exibir mensagem de sucesso após alteração
- ✅ **CA002.6** - Sistema deve exibir erro se placa não for encontrada

---

### US003 - Buscar Veículo por Nome
**Como** usuário do sistema  
**Quero** buscar veículos por parte do modelo/marca  
**Para** encontrar rapidamente o veículo desejado

**Critérios de Aceitação:**
- ✅ **CA003.1** - A busca deve ser case-insensitive
- ✅ **CA003.2** - Deve buscar por parte do texto em modelo e marca
- ✅ **CA003.3** - Deve retornar lista com todos os veículos que contenham o termo
- ✅ **CA003.4** - Deve exibir: placa, modelo, marca, tipo, status (disponível/alugado)
- ✅ **CA003.5** - Se não encontrar resultados, exibir mensagem informativa

---

## 👥 Épico: Gestão de Clientes

### US004 - Cadastrar Cliente Pessoa Física
**Como** atendente da locadora  
**Quero** cadastrar um cliente pessoa física  
**Para** permitir que ele alugue veículos

**Critérios de Aceitação:**
- ✅ **CA004.1** - Deve capturar: nome, CPF, telefone, endereço, email, data nascimento
- ✅ **CA004.2** - CPF deve ser único no sistema (RN6)
- ✅ **CA004.3** - CPF deve ter formato válido (11 dígitos)
- ✅ **CA004.4** - Email deve ter formato válido
- ✅ **CA004.5** - Data de nascimento deve indicar maior idade (≥18 anos)
- ✅ **CA004.6** - Sistema deve exibir erro se CPF já estiver cadastrado

---

### US005 - Cadastrar Cliente Pessoa Jurídica
**Como** atendente da locadora  
**Quero** cadastrar um cliente pessoa jurídica  
**Para** permitir locações empresariais

**Critérios de Aceitação:**
- ✅ **CA005.1** - Deve capturar: razão social, nome fantasia, CNPJ, telefone, endereço, email
- ✅ **CA005.2** - CNPJ deve ser único no sistema (RN6)
- ✅ **CA005.3** - CNPJ deve ter formato válido (14 dígitos)
- ✅ **CA005.4** - Email deve ter formato válido
- ✅ **CA005.5** - Sistema deve exibir erro se CNPJ já estiver cadastrado

---

### US006 - Alterar Cliente
**Como** atendente da locadora  
**Quero** alterar dados de um cliente  
**Para** manter informações atualizadas

**Critérios de Aceitação:**
- ✅ **CA006.1** - Deve permitir buscar por CPF (PF) ou CNPJ (PJ)
- ✅ **CA006.2** - CPF/CNPJ não podem ser alterados (identificadores únicos)
- ✅ **CA006.3** - Demais campos podem ser alterados respeitando validações
- ✅ **CA006.4** - Sistema deve confirmar alterações com sucesso

---

## 🚗 Épico: Gestão de Aluguéis

### US007 - Alugar Veículo
**Como** atendente da locadora  
**Quero** registrar o aluguel de um veículo  
**Para** controlar a locação e cobrança

**Critérios de Aceitação:**
- ✅ **CA007.1** - Deve permitir selecionar cliente (PF ou PJ) e veículo disponível
- ✅ **CA007.2** - Deve registrar: local, data e horário de retirada (RN3)
- ✅ **CA007.3** - Veículo deve estar disponível (não alugado) (RN5)
- ✅ **CA007.4** - Após aluguel, veículo fica indisponível (RN5)
- ✅ **CA007.5** - Sistema deve gerar número único para o aluguel
- ✅ **CA007.6** - Deve exibir valor da diária conforme tipo do veículo

**Valores por tipo:**
- PEQUENO: R$ 100,00/dia
- MEDIO: R$ 150,00/dia  
- SUV: R$ 200,00/dia

---

### US008 - Devolver Veículo
**Como** atendente da locadora  
**Quero** registrar a devolução de um veículo  
**Para** calcular o valor final e liberar o veículo

**Critérios de Aceitação:**
- ✅ **CA008.1** - Deve buscar aluguel ativo por placa do veículo ou número do aluguel
- ✅ **CA008.2** - Deve registrar: local, data e horário de devolução (RN3)
- ✅ **CA008.3** - Deve calcular quantidade de diárias (RN4):
  - Qualquer fração de hora conta como diária completa
  - Ex: aluguel às 15h30 do dia 25, devolução até 15h30 do dia 26 = 1 diária
- ✅ **CA008.4** - Deve aplicar descontos conforme regras (RN7):
  - **Pessoa Física**: 5% desconto se > 5 diárias
  - **Pessoa Jurídica**: 10% desconto se > 3 diárias
- ✅ **CA008.5** - Deve exibir resumo: período, diárias, valor bruto, desconto, valor final
- ✅ **CA008.6** - Veículo volta a ficar disponível após devolução

**Cenários de Cálculo:**
- **Cenário 1**: PF - 6 diárias veículo PEQUENO
  - Valor bruto: 6 × R$ 100,00 = R$ 600,00
  - Desconto: 5% = R$ 30,00
  - Valor final: R$ 570,00

- **Cenário 2**: PJ - 4 diárias veículo SUV
  - Valor bruto: 4 × R$ 200,00 = R$ 800,00
  - Desconto: 10% = R$ 80,00
  - Valor final: R$ 720,00

---

## 🎯 Épico: Funcionalidades Extras (Bônus)

### US009 - Paginação de Listagens
**Como** usuário do sistema  
**Quero** navegar por listagens paginadas  
**Para** melhor performance e usabilidade

**Critérios de Aceitação:**
- ✅ **CA009.1** - Listagens devem exibir no máximo 10 itens por página
- ✅ **CA009.2** - Deve mostrar número da página atual e total de páginas
- ✅ **CA009.3** - Deve permitir navegar entre páginas (anterior/próxima)
- ✅ **CA009.4** - Aplica-se a: listagem de veículos, clientes e aluguéis

---

### US010 - Persistência em Arquivos
**Como** administrador do sistema  
**Quero** que os dados sejam salvos em arquivos  
**Para** manter informações entre execuções

**Critérios de Aceitação:**
- ✅ **CA010.1** - Dados devem ser salvos automaticamente após cada operação
- ✅ **CA010.2** - Sistema deve carregar dados ao iniciar
- ✅ **CA010.3** - Deve manter arquivos separados: veiculos.txt, clientes.txt, alugueis.txt
- ✅ **CA010.4** - Deve tratar erros de leitura/escrita

---

## 🎮 Épico: Interface do Sistema

### US011 - Menu Interativo
**Como** usuário do sistema  
**Quero** navegar por um menu intuitivo  
**Para** acessar todas as funcionalidades

**Critérios de Aceitação:**
- ✅ **CA011.1** - Menu principal deve listar todas as opções disponíveis
- ✅ **CA011.2** - Deve permitir voltar ao menu anterior
- ✅ **CA011.3** - Deve validar opções inválidas
- ✅ **CA011.4** - Deve permitir sair do sistema
