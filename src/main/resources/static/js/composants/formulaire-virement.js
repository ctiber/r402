import { envoyer, euros } from '../api.js';

const SEUIL_CONFIRMATION = 1000;

export default {
  name: 'FormulaireVirement',
  props: { comptes: Array },
  emits: ['virement-effectue'],
  data() {
    return { source: '', cible: '', montant: '', message: '', succes: false, aConfirmer: false };
  },
  watch: {
    comptes(liste) {
      if (!this.source && liste.length > 0) this.source = liste[0].numero;
    }
  },
  methods: {
    euros,
    valider() {
      this.message = '';
      if (parseInt(this.montant) > SEUIL_CONFIRMATION) {
        this.aConfirmer = true;
      } else {
        this.envoyer();
      }
    },
    annuler() {
      this.aConfirmer = false;
    },
    async envoyer() {
      this.aConfirmer = false;
      const reponse = await envoyer('/api/virements', {
        source: this.source,
        cible: this.cible.trim(),
        montant: parseInt(this.montant)
      });
      this.succes = reponse.ok;
      if (reponse.ok) {
        this.message = reponse.contenu.message;
        this.montant = '';
        this.$emit('virement-effectue');
      } else if (reponse.statut === 422) {
        this.message = 'Erreur inconnue (422)';
      } else {
        this.message = reponse.contenu.message || 'Erreur ' + reponse.statut;
      }
    }
  },
  template: `
    <section>
      <h2>Nouveau virement</h2>
      <form class="virement" @submit.prevent="valider">
        <label for="source">Compte à débiter</label>
        <select id="source" v-model="source">
          <option v-for="compte in comptes" :key="compte.numero" :value="compte.numero">
            {{ compte.numero }} ({{ euros(compte.solde) }})
          </option>
        </select>

        <label for="cible">Compte à créditer</label>
        <input id="cible" v-model="cible" placeholder="FR76-..." required>

        <span>Montant (€)</span>
        <input v-model="montant" inputmode="decimal" required>

        <span></span>
        <div><button type="submit" class="principal">Valider</button></div>
      </form>
      <p class="message" :class="succes ? 'succes' : 'erreur'" role="status">{{ message }}</p>

      <div v-if="aConfirmer" class="voile">
        <div class="dialogue" role="dialog" aria-modal="true" aria-labelledby="titre-confirmation">
          <h2 id="titre-confirmation">Confirmer le virement</h2>
          <p>Vous allez virer {{ montant }} € du compte {{ source }} vers le compte {{ cible }}.</p>
          <div class="actions">
            <div class="lien-bouton" @click="annuler">Annuler</div>
            <button type="button" class="principal" @click="envoyer">Confirmer</button>
          </div>
        </div>
      </div>
    </section>`
};
