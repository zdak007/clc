import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

class Main {
    public static void main(String[] args) {

// zadání hodnot
        BigDecimal t = new BigDecimal("28.0");           // teplota v °C
        BigDecimal w = new BigDecimal("0.05");           // hmotnostní zlomek (0-1)
        BigDecimal rhoMer = new BigDecimal("1.030");     // naměřená hustota v g/cm3



// zaokrouhlování a výpis zadaných hodnot
        MathContext mc = new MathContext(20, RoundingMode.HALF_UP);

        System.out.println("teplota: " + t + " °C");
        System.out.println("hmotnostní zlomek: " + w);
        System.out.println("--------------------------------------------------");

// výpočet hustoty vody
        BigDecimal t2 = t.pow(2, mc);
        BigDecimal t3 = t.pow(3, mc);
        BigDecimal t4 = t.pow(4, mc);
        BigDecimal t5 = t.pow(5, mc);

        BigDecimal citVoda = new BigDecimal("999.83952")
                .add(new BigDecimal("16.945176").multiply(t, mc))
                .subtract(new BigDecimal("7.9870401E-3").multiply(t2, mc))
                .subtract(new BigDecimal("46.170461E-6").multiply(t3, mc))
                .add(new BigDecimal("105.56302E-9").multiply(t4, mc))
                .subtract(new BigDecimal("280.54253E-12").multiply(t5, mc));

        BigDecimal jmenVoda = BigDecimal.ONE.add(new BigDecimal("16.879850E-3").multiply(t, mc));
        BigDecimal rhoH2O = citVoda.divide(jmenVoda, mc);

        System.out.println("hustota vody: " + rhoH2O.setScale(10, RoundingMode.HALF_UP) + " kg/m3");

// zdánlivý měrný objem soli
        BigDecimal c0 = new BigDecimal("-0.00433");
        BigDecimal c1 = new BigDecimal("0.06471");
        BigDecimal c2 = new BigDecimal("1.01660");
        BigDecimal c3 = new BigDecimal("0.014624");
        BigDecimal c4 = new BigDecimal("3315.6");

        BigDecimal vAppNum = w.add(c2, mc).add(c3.multiply(t, mc), mc);

        BigDecimal bracket = c0.multiply(w, mc).add(c1, mc);
        BigDecimal tPlusC4 = t.add(c4, mc);

        BigDecimal expArg = new BigDecimal("1E-6").multiply(tPlusC4.pow(2, mc), mc);
        BigDecimal expVal = BigDecimal.valueOf(Math.exp(expArg.doubleValue()));

        BigDecimal vAppDenom = bracket.multiply(expVal, mc);
        BigDecimal vApp = vAppNum.divide(vAppDenom, mc);

        System.out.println("Zdánlivý měrný objem: " + vApp.setScale(10, RoundingMode.HALF_UP) + " m3/kg");

// výpočet hustoty roztoku
        BigDecimal oneMinusW = BigDecimal.ONE.subtract(w);
        BigDecimal term1 = oneMinusW.divide(rhoH2O, mc);
        BigDecimal term2 = w.multiply(vApp, mc);

        BigDecimal rhoSolKgM3 = BigDecimal.ONE.divide(term1.add(term2, mc), mc);
        BigDecimal rhoSolGCm3 = rhoSolKgM3.divide(new BigDecimal("1000"), mc);

        System.out.println("teoretická hustota roztoku: " + rhoSolKgM3.setScale(10, RoundingMode.HALF_UP) + " kg/m3");
        System.out.println("teoretická hustota roztoku: " + rhoSolGCm3.setScale(10, RoundingMode.HALF_UP) + " g/cm3");

// výpočet chyby
        BigDecimal delta = rhoMer.subtract(rhoSolGCm3);
        BigDecimal relError = delta.abs().divide(rhoSolGCm3, mc).multiply(new BigDecimal("100"), mc);

        System.out.println("--------------------------------------------------");
        System.out.println("Absolutní chyba: " + delta.setScale(10, RoundingMode.HALF_UP) + " g/cm3");
        System.out.println("Relativni chyba: " + relError.setScale(10, RoundingMode.HALF_UP) + " %");
    }
}