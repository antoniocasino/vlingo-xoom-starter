<script>
	import { onMount } from 'svelte';
	import { theme, isMobile } from '../stores';
	import { Button, Icon, MaterialApp, AppBar, Container } from "svelte-materialify/src";
	import { mdiMenu, mdiWeatherNight, mdiWeatherSunny } from '@mdi/js';
	import SiteNavigation from '../components/SiteNavigation.svelte';
	export let segment;

	let sidenav = false;
	const toggleTheme = () => $theme = ($theme === "light") ? "dark" : "light";
	$: bgTheme = ($theme === "light") ? "#ffffff" : "#212121";

	onMount(() => {
		isMobile.check();
	})
</script>

<svelte:window on:resize={isMobile.check} />

<div style="height: 100%; background-color: {bgTheme}">
<MaterialApp theme={$theme}>
	<AppBar fixed style="width:100%"> <!-- class={'primary-color theme--dark'} -->
    	<div slot="icon">
    	  {#if $isMobile}
    	    <Button fab depressed on:click={() => (sidenav = !sidenav)} aria-label="Open Menu">
    	    	<Icon path={mdiMenu} />
    	    </Button>
    	  {/if}
		</div>
		<a href="context" slot="title" class="text--primary"><span style="color: var(--theme-text-primary);"> VLINGO/XOOM Starter </span></a>
		<div style="flex-grow:1" />
    	<!-- <a
    	  href="https://github.com/TheComputerM/svelte-materialify"
    	  target="_blank"
    	  rel="noopener">
    	  <Button class="white-text grey darken-3" aria-label="GitHub" fab={mobile}>
    	    <Icon path={mdiGithub} class={!mobile ? 'mr-3' : ''} />
    	    {#if !mobile}GitHub{/if}
    	  </Button>
    	</a> -->
    	<Button fab text on:click={toggleTheme} aria-label="Toggle Theme">
    		<Icon path={$theme === "light" ? mdiWeatherNight : mdiWeatherSunny}/>
    	</Button>
	</AppBar>

	<SiteNavigation {segment} bind:mobile={$isMobile} bind:sidenav />

	<main class:navigation-enabled={!$isMobile}>
		<Container>
    	<!-- {#if ...}
    		<Loading />
    	{/if} -->
		<slot />
		</Container>
  	</main>
</MaterialApp>
</div>

<style lang="scss" global>
	main {
	  padding-top: 5rem;
	}
	.navigation-enabled {
	  padding: 5rem 5rem 5rem calc(256px + 5rem);
	}

	.error-text {
		.s-input__details {
			color: inherit;
		}
	}
</style>
