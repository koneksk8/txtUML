package hp;

import hp.model.MagicBox;
import hp.model.MagicBox.*;
import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Diamond;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.South;
import hu.elte.txtuml.api.layout.Spacing;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.api.layout.West;

public class MagicBoxSMDiagram extends StateMachineDiagram<MagicBox> {

	public class P_Below_Privet extends Phantom{}
	public class P_Below_Kings extends Phantom {}
	public class P_RightOf_Abszol extends Phantom {}
	public class P_RightOf_Gringo extends Phantom {}
	
	@Spacing(0.5)
	@Diamond(left = Privet_Drive.class, top = Godrics_Hollow.class,
			right = Abszol_ut.class, bottom = Kings_Cross.class)
	@Column({Init.class, Privet_Drive.class,
		P_Below_Privet.class, Gyengelkedo.class})
	@Column({Kings_Cross.class, P_Below_Kings.class,
		Azkaban.class})
	@Row({Abszol_ut.class, P_RightOf_Abszol.class,
		Roxfort_express.class})
	@Row({Gringotts.class, P_RightOf_Gringo.class,
		Odu.class, Kviddics_palya.class, Cel.class})
	@Below(val = Gringotts.class, from = P_RightOf_Abszol.class)
	@Column({Piton_szobaja.class, Roxfort.class,
		Roxfort_express.class})
	@Row({Hagrid_kunyhoja.class, Piton_szobaja.class,
		Harom_sepru.class, Titkok_kamraja.class})
	@Row({Roxfort.class, Tiltott_rengeteg.class})
	@Column({Odu.class, Magiaugyi_Miniszterium.class})
	@West(val = Levicorpus_HaAz.class, from = Azkaban.class)
	@North(val = Silencio_GrGo.class, from = Godrics_Hollow.class)
	@South(val = ExpectoPatronum_GrAz.class, from = Azkaban.class)
	@South(val = Silencio_RoEAz.class, from = Azkaban.class)
	@North(val = Locomotor_TiPi.class, from = Piton_szobaja.class)
	@North(val = Disaudio_KvPi.class, from = Piton_szobaja.class)
	public class MagixBoxLayout extends Layout {}
	
}

