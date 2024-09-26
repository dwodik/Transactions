import java.util.Random;

public class StartThread extends Thread{

    int accountsCount;
    long maxMoney;
    int maxSumTransfer;
    Bank bank;
    Random random;
    Random ran;

    public StartThread(int accountsCount, long maxMoney, int maxSumTransfer, Bank bank, Random random, Random ran) {
        this.accountsCount = accountsCount;
        this.maxMoney = maxMoney;
        this.maxSumTransfer = maxSumTransfer;
        this.bank = bank;
        this.random = random;
        this.ran = ran;
    }

    @Override
    public void run() {
            int number1 = random.nextInt(accountsCount); // подбираем номер первого аккаунта
            String acc1 = Integer.toString(number1);
            int number2 = random.nextInt(accountsCount);// подбираем номер второго аккаунта
            String acc2 = Integer.toString(number2);
            long sumTransfer = ran.nextInt(maxSumTransfer); // задаем сумму перевода
            try {
                bank.transfer(acc1, acc2, sumTransfer); // вызываем перевод денег с одного счета на другой
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

    }
}
