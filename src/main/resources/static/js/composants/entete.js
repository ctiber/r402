import { seDeconnecter } from '../api.js';

export default {
  name: 'EnTete',
  props: { utilisateur: Object },
  methods: { seDeconnecter },
  template: `
    <header>
      <h1>Ma banque</h1>
      <div v-if="utilisateur">
        <span>Bonjour, {{ utilisateur.nom }}</span>
        &nbsp;
        <button type="button" @click="seDeconnecter">Se déconnecter</button>
      </div>
    </header>`
};
