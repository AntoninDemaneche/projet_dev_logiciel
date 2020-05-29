# Projet dev logiciel

## Plugin minecraft

Notre plugin minecraft avais pour but d'avoir un profil à part entière pour chaque joueur connecté au serveur, il a la possibillité d'avoir des sorts, un chat recréer avec une certaine distance pour parler.

### Installation du plugin

1. En premier temps, pour l'installation du plugin il est nécessaire d'avoir un serveur minecraft qui fonctionne en local ou chez un hébergement.

2. Il faudra aussi une base de donnée pour pouvoir stocké toutes les informations nécessaire comme le profil de chaque joueur.

3. Une fois tous celà fais il vous suffit juste de mettre le fichier `Plugin.jar` dans le dossier `Plugins` de votre serveur, lancer votre serveur, le serveur va s'arrêter tous seul une seconde fois mais un dossier du nom de `Plugin` va apparaître et il faudra à l'intérieur modifié le nécessaire pour la connexion à la BDD.

4. L'étape 3 terminé, relancer une dernière fois votre serveur et le plugin se lance automatiquement 

### Feature du plugin 

Commande en jeu :

- /profil (qui permet d'accèder à votre profil avec toute vos information).
- /rank (pour modifier le rang d'un joueur et donc lui faire gagner du mana).
- /jutsu (ici votre liste de sort appris avec les parchemins).
- /boots (qui permet de courir plus vite et de marcher sur l'eau).

Autre :

- Une gestion de l'argent avec les Ryôs
- Un échange de monnaie avec des PNJ et des vendeurs
- Un petit casino
- La possibilité de s'asseoir
- Le chat local
- Les différentes maîtrise de techniques