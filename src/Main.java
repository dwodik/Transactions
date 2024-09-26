import java.util.ArrayList;
import java.util.Random;

public class Main {
    static int accountsCount = 5000; // количество аккаунтов
    static long maxMoney = 1_000_000; // количество денег в банке, а точнее предел стартового капитала на аккаунтах
    static int transferAmount = 500; // количество переводов
    static int maxSumTransfer = 150_000; // максимальная сумма перевода

    static Bank bank = new Bank(accountsCount, maxMoney); // создаем банк
    static Random random = new Random(1); // счетчик начинается с первого аккаунта
    static Random ran = new Random(1);

    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();
        ArrayList<Thread> threads = new ArrayList<>(); // сюда запишем все потоки (чтобы потом их соединить)

        for (int i = 0; i < transferAmount; i++) { // задаем цикл по количеству переводов
            StartThread st = new StartThread(accountsCount, maxMoney, maxSumTransfer, bank, random, ran); // создал отдельный класс для запуска потока, чтобы потом можно было соединить их
            st.start(); // запускаем
            threads.add(st); // добавляем в список потоков
        }
        for (Thread thread : threads) { // перебираем все потоки и соединяем (чтобы корректно работал подсчёт времени выполнения программы и чтобы корректно подсчитывался итог)
            thread.join();
        }
        bank.setBlockedAccountsAccounts(); // вывод заблокированных аккаунтов
        System.out.println("Длина операции: " + ((System.currentTimeMillis() - start) / 1000) + " с");
    }

    // ОДНОПОТОЧНЫЙ ОТСТРОЕННЫЙ РЕЖИМ
        /*long start = System.currentTimeMillis();

        Bank bank = new Bank(accountsCount, maxMoney); // создаем банк
        Random random = new Random(1); // счетчик начинается с первого аккаунта
        Random ran = new Random(1);

        for (int i = 0; i < transferAmount; i++) { // задаем цикл по количеству переводов

            int number1 = random.nextInt(accountsCount); // подбираем номер первого аккаунта
            String acc1 = Integer.toString(number1);
            int number2 = random.nextInt(accountsCount);// подбираем номер второго аккаунта
            String acc2 = Integer.toString(number2);

            long sumTransfer = ran.nextInt(maxSumTransfer); // задаем сумму перевода
            try {
                bank.transfer(acc1, acc2, sumTransfer); // вызываем перевода денег с одного счета на другой
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        bank.setBlockedAccountsAccounts();
        System.out.println("Длина операции: " + ((System.currentTimeMillis() - start) / 1000) + " с");
    }*/
}
