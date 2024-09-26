import java.util.HashMap;
import java.util.Random;

public class Bank
{
    private final HashMap<String, Account> accounts = new HashMap<>();
    private final HashMap<String, Account> blockedAccounts = new HashMap<>();
    private final Random random = new Random();

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Thread.sleep(100);
        return random.nextBoolean();
    }

    public Bank(int accountsCount, long maxMoney) {
        createAccount(accountsCount, maxMoney);
    }


    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public synchronized void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException { // метод перевода денег со счета на счет
        // принимает номера двух аккаунтов и количество переводимых денег. использование метода isFraud потребовало включить интерраптедЭксепшн
        // - как я понял выбрасывается если это событие прерывается другим потоком
        // так же добавил ключевое слово synchronized - выкидывало NullPointerException - synchronized позволил гарантировать чтобы метод выполнялся одновременно только в одном потоке
        if (blockedAccounts.containsKey(fromAccountNum) || blockedAccounts.containsKey(toAccountNum)) {
            return; // рандомно выбрасывались повторно аккаунты, которые уже заблокированы. поэтому пришлось сделать проверку. если аккаунт заблокирован, то выходим из метода
        }

        if (amount > 50_000) { // если сумма перевода больше 50к то СБ проверяет данный перевод

            if (isFraud(fromAccountNum, toAccountNum, amount)) { // если СБ говорит true
                accounts.remove(fromAccountNum); // то блокируем аккаунты. я решил удалить из этого мап и добавить в мап заблокированных аккаунтов
                blockedAccounts.put(fromAccountNum, accounts.get(fromAccountNum));
                accounts.remove(toAccountNum);
                blockedAccounts.put(toAccountNum, accounts.get(toAccountNum));
                System.out.println("СБ ЗАБЛОКИРОВАЛА АККАУНТЫ: " + fromAccountNum + " и " + toAccountNum + " ИЗ-ЗА ПЕРЕВОДА БОЛЕЕ 50К" +
                        " - " + amount + "Р, ТЕПЕРЬ ОНИ В ЧЕРНОМ СПИСКЕ!");
                return; // если произошла блокировка аккаунтов - выходим из метода
            } else { // иначе (если isFraud вернул false - продолжаем
                System.out.print("ПРОВЕРКУ В СБ ПЕРЕВОД ПРОШЁЛ, выполняется перевод от: " + fromAccountNum + ", кому: " + toAccountNum + " на сумму " + amount + " руб. Было " +
                        getBalance(fromAccountNum) + "/" + getBalance(toAccountNum));
            }

        } else { // если сумма перевода меньше 50к и соответственно СБ не проверяет перевод, то выполняем перевод ДС
            System.out.print("Выполняется перевод от: " + fromAccountNum + ", кому: " + toAccountNum + " на сумму " + amount + " руб. Было " +
                    getBalance(fromAccountNum) + "/" + getBalance(toAccountNum));
        }
        long fromMoney = accounts.get(fromAccountNum).getMoney(); // выводим сумму на счёте откуда идёт перевод
        long toMoney = accounts.get(toAccountNum).getMoney(); // выводим сумму на счёте куда идет перевод
        accounts.get(fromAccountNum).setMoney(fromMoney - amount); // устанавливаем новую сумму на счетах аккаунтов
        accounts.get(toAccountNum).setMoney(toMoney + amount);
        System.out.println(" стало " + getBalance(fromAccountNum) + "/" + getBalance(toAccountNum));
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        return accounts.get(accountNum).getMoney();
    }
    private void createAccount(int accountsCount, long maxMoney) { // метод создания аккаунтов и заполнение мапа данными
        Random rand = new Random(1); // запускаем рэндом не более заданной суммы
        for (int i = 1; i <= accountsCount; i++) { // создаем цикл длиной с заданным количеством аккаунтов
            long money =  rand.nextInt((int) maxMoney); // присваиваем количество денег
            String numAccount = String.valueOf(i);
            Account acc = new Account(money, numAccount); // создаём аккаунт, передаем ему количество денег и номер аккаунта. зачем был задан номер аккаунта стрингом?
            accounts.put(numAccount, acc); // добавляем этот аккаунт в мап
        }
    }
    public void setBlockedAccountsAccounts() {
        System.out.println("Заблокированные аккаунты: " + blockedAccounts.size() + " штук");
        System.out.println(blockedAccounts.keySet());
    }
}
