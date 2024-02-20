package game.entity.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Stat {

    public float vie;

    public float vieMax;

    public float mana;

    public float manaMax;

    public int atkMin, atkMax;

    public int defMin, defMax;

    public float crit; // Pourcentage

    public float regenVie, regenMana; // Recupération par seconde ou par cycle

    public Stat() {

    }

    public Stat(final int vie, final int mana, final float critique) {
        this.vie = vieMax = vie;
        this.mana = manaMax = mana;
        this.crit = critique;

        regenVie = regenMana = 0;
    }

    public Stat(final Stat toCopy) {
        vie = toCopy.vie;
        vieMax = toCopy.vieMax;
        mana = toCopy.mana;
        manaMax = toCopy.manaMax;
        atkMin = toCopy.atkMin;
        atkMax = toCopy.atkMax;
        defMin = toCopy.defMin;
        defMax = toCopy.defMax;
        crit = toCopy.crit;
        regenVie = toCopy.regenVie;
        regenMana = toCopy.regenMana;
    }

    public Stat(final Stat base, final Stat variance) {
        final float lifeBuf = (float) (Math.random() * variance.vie * 3) - variance.vie,
                manaBuf = (float) (Math.random() * variance.mana * 3) - variance.mana;

        final int atkBuf = (int) (Math.random() * variance.atkMin * 3) - variance.atkMin,
                defBuf = (int) (Math.random() * variance.defMin * 3) - variance.defMin;

        final float critBuf = (float) (Math.random() * variance.crit * 3) - variance.crit,
                regenVieBuf = (float) (Math.random() * variance.regenVie * 3) - variance.regenVie,
                regenManaBuf = (float) (Math.random() * variance.regenMana * 3) - variance.regenMana;

        vie = vieMax = base.vie + lifeBuf;
        mana = manaMax = base.mana + manaBuf;

        atkMin = base.atkMin + atkBuf;
        atkMax = base.atkMax + atkBuf;
        defMin = base.defMin + defBuf;
        defMax = base.defMax + defBuf;

        crit = base.crit + critBuf;
        regenVie = base.regenVie + regenVieBuf;
        regenMana = base.regenMana + regenManaBuf;
    }

    public Stat(final DataInputStream dis) throws IOException {
        vie = dis.readFloat();
        vieMax = dis.readFloat();
        mana = dis.readFloat();
        manaMax = dis.readFloat();
        atkMin = dis.readInt();
        atkMax = dis.readInt();
        defMin = dis.readInt();
        defMax = dis.readInt();
        crit = dis.readFloat();
        regenVie = dis.readFloat();
        regenMana = dis.readFloat();
    }

    public void write(final DataOutputStream dos) throws IOException {
        dos.writeFloat(vie);
        dos.writeFloat(vieMax);
        dos.writeFloat(mana);
        dos.writeFloat(manaMax);
        dos.writeInt(atkMin);
        dos.writeInt(atkMax);
        dos.writeInt(defMin);
        dos.writeInt(defMax);
        dos.writeFloat(crit);
        dos.writeFloat(regenVie);
        dos.writeFloat(regenMana);
    }

    public String toString() {
        final StringBuilder str = new StringBuilder("vie:" + vie + "/" + (int) vieMax);

        str.append(", mana:").append(mana).append("/").append((int) manaMax);
        str.append(", atk:").append(atkMin).append("-").append(atkMax);
        str.append(", def:").append(defMin).append("-").append(defMax);
        str.append(", crit:").append(crit * 100).append("%");
        str.append(", regen:").append(regenVie).append("/s ").append(regenMana).append("/s");

        return str.toString();
    }
}
