import java.math.BigInteger;
import java.util.Random;

public class Server {
    public static void main(String[] args) 
    {
        Boolean debug=false;
    	if(args.length != 2) { System.out.println("Invalid arguments, exiting..."); return; }
    	
        String filename = args[0];
        String clientFilename = args[1];
	
        Inputs inputs = new Inputs(filename);

        BigInteger[] serverInputs = inputs.getInputs();

        BigInteger[] encryptedPolyCoeffs = (BigInteger[])StaticUtils.read(clientFilename);
        BigInteger publicKey = (BigInteger)StaticUtils. read("ClientPK.out");

        Paillier paillier = new Paillier();
        paillier.setPublicKey(publicKey);

        BigInteger[] encryptedPolyEval = new BigInteger[serverInputs.length];
        
        /* TODO: implement server-side protocol here.
         * For each sj in serverInputs:
			- Pick a random rj
			- Homomorphically evaluate P(sj)
			- Compute E_K(rj P(sj) + sj)
			- Set encryptedPolyEval[j] = E_K(rj P(sj) + sj)
        */
 	// ------ Your code goes here. --------
	BigInteger rj = BigInteger.ZERO;
	BigInteger P_sj = BigInteger.ZERO;
        for (int j = 0; j < serverInputs.length; j++) {
            rj = randomBigInt(paillier.n); 
            P_sj = homoPolynomial(serverInputs[j], encryptedPolyCoeffs, paillier);
            encryptedPolyEval[j] = paillier.Encryption(P_sj.add(serverInputs[j]));
        }

        StaticUtils.write(encryptedPolyEval, clientFilename+".out");
    }

    //This is not cryptographically secure random number.
    public static BigInteger randomBigInt(BigInteger n) 
    {
        Random rand = new Random();
        BigInteger result = new BigInteger(n.bitLength(), rand);
        while( result.compareTo(n) >= 0 ) {
            result = new BigInteger(n.bitLength(), rand);
        }
        return result;
    }

    private static BigInteger homoPolynomial(BigInteger sj, BigInteger[] encryptedCoefficients, Paillier paillier) 
    {
        BigInteger eval = BigInteger.ZERO;
        BigInteger sj_n = BigInteger.ONE;

        for (int i = 0; i < encryptedCoefficients.length; i++) {
            eval = paillier.add(eval, encryptedCoefficients[i].multiply(sj_n));
            sj_n = sj_n.multiply(sj);
        }

        return eval;
    }
}
