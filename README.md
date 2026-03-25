# 📦 CRUD com JPA + Hibernate

Sistema de gerenciamento de usuários via terminal, utilizando **Jakarta Persistence API (JPA)** com **Hibernate** como provedor ORM e **MySQL** como banco de dados.

---

## 🛠 Tecnologias

- Java 17+
- Jakarta Persistence API 3.2
- Hibernate 6.6
- MySQL 8+
- Maven

---

## 📁 Estrutura do Projeto

```
projeto-jpa/
├── src/
│   └── main/
│       ├── java/
│       │   └── br/com/krono/
│       │       ├── modelo/
│       │       │   └── Usuario.java       # Entidade JPA
│       │       └── teste/
│       │           └── CrudUsuario.java   # Classe principal
│       └── resources/
│           └── META-INF/
│               └── persistence.xml        # Configuração JPA
└── pom.xml
```

---

## ⚙️ Configuração

### 1. Banco de dados

Crie o banco no MySQL:

```sql
CREATE DATABASE nome_do_banco;
```

### 2. persistence.xml

Edite o arquivo `src/main/resources/META-INF/persistence.xml` com suas credenciais:

```xml
<property name="jakarta.persistence.jdbc.url"
          value="jdbc:mysql://localhost:3306/nome_do_banco" />
<property name="jakarta.persistence.jdbc.user" value="seu_usuario" />
<property name="jakarta.persistence.jdbc.password" value="sua_senha" />
```

> ⚠️ **Nunca versione senhas reais.** Adicione o `persistence.xml` ao `.gitignore` ou use variáveis de ambiente.

A propriedade `hibernate.hbm2ddl.auto = update` faz o Hibernate criar/atualizar a tabela automaticamente na primeira execução.

### 3. Dependências (pom.xml)

```xml
<dependencies>
    <dependency>
        <groupId>jakarta.persistence</groupId>
        <artifactId>jakarta.persistence-api</artifactId>
        <version>3.2.0</version>
    </dependency>
    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>6.6.0.Final</version>
    </dependency>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>9.0.0</version>
    </dependency>
</dependencies>
```

---

## 🗂 Entidade `Usuario`

```java
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nome_usuario", nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;
}
```

| Anotação | Função |
|---|---|
| `@Entity` | Mapeia a classe para uma tabela no banco |
| `@Id` | Define a chave primária |
| `@GeneratedValue` | ID gerado automaticamente pelo banco (`AUTO_INCREMENT`) |
| `@Column(name=...)` | Mapeia o atributo para uma coluna com nome diferente |
| `nullable = false` | Coluna `NOT NULL` no banco |
| `unique = true` | Garante valores únicos na coluna |

Tabela gerada pelo Hibernate:

```sql
CREATE TABLE usuario (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    nome_usuario VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);
```

---

## 🔁 Operações CRUD

### Inserir
```java
em.getTransaction().begin();
em.persist(new Usuario(nome, email));
em.getTransaction().commit();
```

### Listar todos
```java
List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u", Usuario.class)
                        .getResultList();
```
> A consulta usa **JPQL** — o nome é da classe Java (`Usuario`), não da tabela SQL.

### Buscar por ID
```java
Usuario usuario = em.find(Usuario.class, id); // retorna null se não encontrar
```

### Atualizar
```java
Usuario usuario = em.find(Usuario.class, id);
usuario.setNome(novoNome);
usuario.setEmail(novoEmail);

em.getTransaction().begin();
em.merge(usuario);
em.getTransaction().commit();
```

### Remover
```java
Usuario usuario = em.find(Usuario.class, id);

em.getTransaction().begin();
em.remove(usuario);
em.getTransaction().commit();
```

---

## 📋 Referência — EntityManager

| Método | Descrição |
|---|---|
| `em.persist(obj)` | Insere uma nova entidade no banco |
| `em.find(Classe, id)` | Busca pelo ID; retorna `null` se não existir |
| `em.merge(obj)` | Atualiza uma entidade existente |
| `em.remove(obj)` | Remove a entidade do banco |
| `em.createQuery(...)` | Cria consulta JPQL |
| `em.getTransaction()` | Acessa a transação atual |

---

## ⚠️ Pontos de Atenção

**Query executada duas vezes no case 2 (Listar):**

```java
// ❌ Errado — executa a query duas vezes
query.getResultList().forEach(...);
if (query.getResultList().isEmpty()) { ... }

// ✅ Correto — armazena o resultado e reutiliza
List<Usuario> lista = query.getResultList();
if (lista.isEmpty()) {
    System.out.println("Nenhum usuário encontrado!");
} else {
    lista.forEach(u -> System.out.println(u.getId() + ": " + u.getNome()));
}
```

**Fechar os recursos ao sair:**

```java
case 0:
    em.close();
    emf.close();
    sc.close();
    break;
```

---

## 📖 Glossário

| Termo | Definição |
|---|---|
| **JPA** | Especificação Java para ORM. Define interfaces e anotações padrão. |
| **Hibernate** | Implementação da JPA. Traduz objetos Java para SQL automaticamente. |
| **ORM** | Mapeamento entre objetos Java e tabelas do banco de dados. |
| **EntityManager** | Interface central da JPA para todas as operações de persistência. |
| **JPQL** | Linguagem de consulta da JPA baseada em classes, não em tabelas SQL. |
| **Transação** | Unidade atômica de trabalho — confirma ou reverte todas as operações. |
| **hbm2ddl.auto** | Propriedade que controla criação/atualização automática do schema. |
